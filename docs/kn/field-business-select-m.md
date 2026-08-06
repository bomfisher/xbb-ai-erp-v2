# BUSINESS 业务单选字段设计与后端渲染实现

## 1. 适用范围

本文说明 `xbb.ai.erp.base.common.filed.FieldTypeEnum#BUSINESS`（数值 `16`）的设计原则、字段元数据协议以及后端如何提供业务选择所需的渲染配置。

`BUSINESS` 表示“从某个业务对象中选择一条或多条数据”，不是普通下拉框。选项来自业务查询接口，前端不能仅依赖 `itemList` 静态渲染。

> 选择产品 Product 时禁止使用 `BUSINESS`。产品字段必须使用 `xbb.ai.erp.base.common.filed.FieldTypeEnum#PRODUCT`（数值 `50`），由产品专用选择器和产品字段协议负责渲染。`BUSINESS` 仅用于可通过通用业务选择协议接入的其他业务对象。

## 2. 设计原则

### 2.1 字段类型只表达交互语义

- `BUSINESS` 表达业务数据选择，不绑定具体业务模块、表名或前端组件实现。
- 字段值保存业务对象的稳定 ID；展示文案由业务选择接口返回的 `label`、`name` 或 `code` 组成。
- 业务对象的过滤、权限、启用状态和排序由对应领域服务负责，不能在通用字段层硬编码。
- 单选使用 `BUSINESS`；多选使用 `BUSINESS_MULTI`。多选的类型编码应与枚举定义保持一致，并由前端根据配置中的 `multiple` 渲染。
- 成员单选使用 `USER(12)`；成员多选使用 `USER_MULTI(13)`，与通用业务单选 `BUSINESS(16)` 分开建模。

### 2.2 元数据驱动渲染

后端返回 `FieldEntity` 时，除 `fieldType` 外还要提供 `businessSelectConfig`。前端据此决定调用哪个业务选择提供者，不通过字段名称猜测业务类型。

配置至少包含：

| 属性 | 说明 |
| --- | --- |
| `businessType` | 业务选择类型，例如 `supplier`、`member` |
| `quickSearchUrl` | 输入关键字时的快捷搜索接口 |
| `dialogSearchUrl` | 弹窗分页搜索接口 |
| `getByIdUrl` | 已有 ID 回显接口 |
| `requestPayload` | 固定业务上下文，例如 `corpid` |
| `placeholder` | 输入框占位文案 |
| `dialogTitle` | 选择弹窗标题 |
| `multiple` | 是否允许多选 |

接口 URL 和业务上下文由后端下发，避免前端复制模块路由规则；同一字段在新建、编辑和回显场景使用同一份配置。

## 3. 后端字段元数据实现

### 3.1 字段实体

公共字段模型为 `xbb.ai.erp.base.common.filed.FieldEntity`，关键属性如下：

```java
field.setFieldType(String.valueOf(FieldTypeEnum.BUSINESS.getType()));
field.setBusinessSelectConfig(config);
```

`BusinessSelectConfig` 使用 Lombok 管理 getter/setter，`multiple` 使用 `Boolean` 表达配置开关；数据库字段和业务对象 ID 仍按各业务模块自身规范处理。

### 3.2 配置构造

建议在业务模块的 application/support 或 assembler 层集中构造配置，不在 Controller 中拼装：

```java
FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
config.setBusinessType("supplier");
config.setQuickSearchUrl("/erp/v1/supplier/businessSelect/quickSearch");
config.setDialogSearchUrl("/erp/v1/supplier/businessSelect/dialogSearch");
config.setGetByIdUrl("/erp/v1/supplier/businessSelect/getById");
config.setRequestPayload(Map.of("corpid", corpid));
config.setPlaceholder("请选择供应商");
config.setDialogTitle("选择供应商");
config.setMultiple(false);
```

字段构造流程应保持：领域字段定义 → assembler/support 生成 `FieldEntity` → 保存/详情接口包装为 `SaveItemVO` 或等价 VO → 前端动态表单渲染。

### 3.3 三类查询接口

每个业务选择提供者应提供以下能力，接口参数使用 DTO，返回统一 `ResultVO.success()` 包装：

1. `quickSearch`：根据关键字返回轻量候选列表，适合输入联想。
2. `dialogSearch`：返回分页列表，可附带 `headList` 作为弹窗表头。
3. `getById`：根据 `corpid + id` 回显已有值；不存在时返回空值或按领域约定抛出 `BizException`。

候选 VO 建议包含 `id`、`code`、`name`、`label`，需要联动回填时增加 `linePatch`。查询必须复用领域权限和状态条件，避免通用选择接口绕过业务规则。

相关产品 SKU 接口事实文档：

- `docs/api/endpoints/product-business-select-quick-search.md`
- `docs/api/endpoints/product-business-select-dialog-search.md`
- `docs/api/endpoints/product-business-select-get-by-id.md`

相关成员单选接口事实文档：

- `docs/api/endpoints/org-quick-search.md`
- `docs/api/endpoints/org-dialog-search.md`
- `docs/api/endpoints/org-get-by-id.md`

## 4. Product 类型边界

产品 Product 是专用字段类型，不属于通用 `BUSINESS`：

- 产品选择字段使用 `FieldTypeEnum.PRODUCT`，编码为 `50`。
- 产品选择器可携带 SKU、规格、采购/销售属性及行回填信息，字段协议由产品模块维护。
- 产品字段不得为了复用通用组件而改成 `BUSINESS(16)`，也不得通过 `businessType = product` 绕过 `PRODUCT` 类型约束。
- 产品字段通过 `FieldEntity.ProductSelectConfig` 下发 `productType`、`businessCode`、业务上下文、占位文案和多选能力；其中不得下发接口 URL。
- 产品接口 URL 只维护在前端 `apps/admin-web/src/components/form/productSelectApi.ts`，产品弹窗及采购明细表格均按接口或单据表单返回的 `headList` 动态渲染。
- 新增产品、SKU 或产品相关字段时，先在产品字段枚举/元数据中声明 `PRODUCT`，再由前端实现产品专用渲染分支。

代码评审至少检查：字段类型是否为 `PRODUCT(50)`、产品接口是否使用产品专属 DTO/VO、是否误挂 `businessSelectConfig`。

## 5. 验收清单

- `BUSINESS` 字段有稳定 ID 值，不把展示文本当作主值。
- `FieldEntity` 同时返回 `fieldType` 和完整 `businessSelectConfig`。
- 快捷搜索、弹窗搜索、按 ID 回显均携带必要的 `corpid` 等上下文。
- 后端主动业务错误统一使用 `BizException`，接口统一使用 `ResultVO.success()`。
- 产品 Product 字段明确使用 `FieldTypeEnum.PRODUCT`，前后端文档和代码不得将其归入 `BUSINESS`。
