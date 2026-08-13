# PRODUCT 与 USER 字段类型前后端 Harness 指导

> **当前协议（2026-08-12）**：`PRODUCT(50)` 是产品专用选择字段，具体 SKU 选择必须使用 `productSelectConfig`。主子档容器使用 `SUB_ITEM(49)` 和 `subField`，不得复用 `PRODUCT(50)`。

## 1. 适用范围

本文用于在 ERP 业务模块新增或扩展动态表单、列表筛选和业务选择字段时，统一实现 `FieldTypeEnum.PRODUCT`、`FieldTypeEnum.USER` 与 `FieldTypeEnum.DEPT`。

参考实现为 `FieldTypeEnum.BUSINESS_MULTI` 的业务选择交互，但两类字段不能只复制字段类型分支：

| 字段类型 | 类型值 | 选择对象 | 前端渲染器 | 接口地址来源 |
| --- | ---: | --- | --- | --- |
| `USER` | `12` | 企业内启用且在职的员工 | `BusinessDataSelectField` | 后端 `businessSelectConfig` 或已注册 Provider |
| `DEPT` | `14` | 企业内启用的部门 | `BusinessDataSelectField` | 后端 `businessSelectConfig` 或已注册 Provider |
| `PRODUCT` | `50` | 产品/SPU/SKU，按业务上下文过滤 | `ProductDataSelectField` | 前端 `productSelectApi.ts` |
| `BUSINESS_MULTI` | 当前枚举值为 `16` | 通用业务对象多选 | `BusinessDataSelectField` | 后端 `businessSelectConfig` |

`USER` 和 `PRODUCT` 的字段值保存稳定 ID，不保存展示名称。展示名称只用于回显和列表显示；产品选择返回的 `linePatch` 可用于回填同一明细行的快照或业务默认值。

## 2. 开发前检查

开始修改前必须完成以下检查：

1. 阅读 `docs/harness/工程规则唯一事实源.md`、`docs/harness/README.md`、`docs/guide/DDD业务模块开发指南.md`。
2. 阅读目标模块字段工厂、字段枚举、保存 DTO/VO、Assembler 和相关 API 原子文档。
3. 阅读前端 `AGENTS.md`、`docs/前端技术规约.md` 以及动态表单和选择器实现。
4. 执行 `git status --short`，确认只修改本需求涉及的文档或代码，不覆盖已有改动。
5. 按仓库约束先确认是否创建 worktree；本次未创建 worktree 时，直接在当前工作区做最小改动。

推荐参考入口：

- 后端枚举：`xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/filed/FieldTypeEnum.java`
- 后端字段模型：`xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/filed/FieldEntity.java`
- 后端通用校验：`xbb-erp-base-common/src/main/java/xbb/ai/erp/base/common/filed/FieldValueValidator.java`
- 前端字段分发：`apps/admin-web/src/components/form/formSchema.ts`
- 前端动态表单：`apps/admin-web/src/components/form/DynamicFormField.vue`
- 前端成员/业务选择器：`apps/admin-web/src/components/form/BusinessDataSelectField.vue`
- 前端产品选择器：`apps/admin-web/src/components/form/ProductDataSelectField.vue`
- 前端产品接口常量：`apps/admin-web/src/components/form/productSelectApi.ts`

## 3. 后端实现规范

### 3.1 字段元数据

字段定义应集中在目标模块的 `application` 层字段枚举、FieldFactory 或 support 类中，由 assembler 组装为 `FieldEntity`。Controller 不直接拼装字段元数据。

#### USER

`USER` 字段必须返回 `fieldType = "12"`，同时返回 `businessSelectConfig`。配置至少包含：

```java
FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
config.setBusinessType("member");
config.setQuickSearchUrl("/erp/v1/org/memberSelect/quickSearch");
config.setDialogSearchUrl("/erp/v1/org/memberSelect/dialogSearch");
config.setGetByIdUrl("/erp/v1/org/memberSelect/getById");
config.setRequestPayload(Map.of("corpid", corpid));
config.setPlaceholder("请选择成员");
config.setDialogTitle("选择成员");
config.setMultiple(false);
field.setFieldType(String.valueOf(FieldTypeEnum.USER.getType()));
field.setBusinessSelectConfig(config);
```

成员接口必须使用 `corpid + id` 作为查询上下文。`id` 以字符串员工 ID 传输，候选对象建议返回 `id`、`code`、`name`、`label`。

#### PRODUCT

`PRODUCT` 字段必须返回 `fieldType = "50"`，同时返回 `productSelectConfig`，不得返回 `businessSelectConfig`，也不得通过 `businessType = "product"` 绕过产品类型约束：

```java
FieldEntity.ProductSelectConfig config = new FieldEntity.ProductSelectConfig();
config.setProductType("product-sku");
config.setBusinessCode(businessCode);
config.setRequestPayload(Map.of(
    "corpid", corpid,
    "businessCode", businessCode
));
config.setPlaceholder("请选择产品");
config.setDialogTitle("选择产品");
config.setMultiple(true);
field.setFieldType(String.valueOf(FieldTypeEnum.PRODUCT.getType()));
field.setProductSelectConfig(config);
```

`ProductSelectConfig` 只下发产品类型、业务码和请求上下文，不下发接口 URL。产品接口地址由前端 `productSelectApi.ts` 统一维护，避免产品字段在不同业务模块复制路由。

### 3.2 选择接口

#### USER 接口

成员选择器至少提供以下三个接口，统一使用 DTO 入参和 `ResultVO.success()` 包装：

| 能力 | 接口 | 返回值 |
| --- | --- | --- |
| 快捷搜索 | `POST /erp/v1/org/memberSelect/quickSearch` | `data: BusinessSelectOption[]` |
| 弹窗搜索 | `POST /erp/v1/org/memberSelect/dialogSearch` | `data: { list, pageHelper }` |
| 按 ID 回显 | `POST /erp/v1/org/memberSelect/getById` | `data: BusinessSelectOption` 或 `null` |

#### DEPT 接口

| 能力 | 接口 | 返回值 |
| --- | --- | --- |
| 快捷搜索 | `POST /erp/v1/org/departmentSelect/quickSearch` | `data: BusinessSelectOption[]` |
| 弹窗搜索 | `POST /erp/v1/org/departmentSelect/dialogSearch` | `data: { list, pageHelper }` |
| 按 ID 回显 | `POST /erp/v1/org/departmentSelect/getById` | `data: BusinessSelectOption` 或 `null` |

完整事实：`docs/api/endpoints/org-department-select-quick-search.md`、`docs/api/endpoints/org-department-select-dialog-search.md`、`docs/api/endpoints/org-department-select-get-by-id.md`。

接口规则：

- `corpid` 必须校验，缺失时抛 `BizException`。
- 快捷搜索按成员姓名或工号匹配，返回轻量候选列表。
- 弹窗搜索返回 `headList`（如需要动态表头）、`list` 和分页信息。
- 只返回启用且在职成员，不能绕过组织权限和员工状态规则。
- 按 ID 回显使用 `corpid + id` 查询，查不到时按接口契约返回 `null`。

完整事实以以下原子文档为准：

- `docs/api/endpoints/org-quick-search.md`
- `docs/api/endpoints/org-dialog-search.md`
- `docs/api/endpoints/org-get-by-id.md`

#### PRODUCT 接口

产品选择器至少提供以下三个接口：

| 能力 | 接口 | 返回值 |
| --- | --- | --- |
| 快捷搜索 | `POST /erp/v1/product/businessSelect/quickSearch` | `data: ProductSelectOption[]` |
| 弹窗搜索 | `POST /erp/v1/product/businessSelect/dialogSearch` | `data: { list, pageHelper }` |
| 按 ID 回显 | `POST /erp/v1/product/businessSelect/getById` | `data: ProductSelectOption` 或 `null` |

接口规则：

- `corpid`、`businessCode`、`productType` 均为必填上下文；当前仅支持 `productType = product-sku`。
- 快捷搜索支持 SKU 编码和 SKU 名称关键字。
- 产品候选返回 `id`、`code`、`name`、`label`；当前不返回 `linePatch`。
- 产品过滤条件（启用状态、可采购/可销售、权限等）由产品领域服务负责。
- `linePatch` 只能回填后端明确允许的字段，前端不得将候选对象任意字段写入表单。

完整事实以以下原子文档为准：

- `docs/api/endpoints/product-business-select-quick-search.md`
- `docs/api/endpoints/product-business-select-dialog-search.md`
- `docs/api/endpoints/product-business-select-get-by-id.md`

### 3.3 保存与校验

- `USER` 保存员工 ID 字符串；不要把 `label`、姓名或工号作为主值。
- `PRODUCT` 保存产品或 SKU 稳定 ID；快照字段由后端根据选择结果或保存规则生成，不信任前端伪造的展示文本。
- 新增、编辑、详情和列表筛选场景使用同一字段语义，差异由 `SceneTypeEnum` 或业务字段工厂表达。
- 字段必填、格式和长度校验继续由 `FieldValueValidator` 处理；领域权限、状态和引用关系由应用/领域层处理。
- 草稿可以保留未完整选择的临时值；正式提交必须重新校验 ID 是否存在、是否属于当前企业及是否满足业务状态。
- 若产品选择为多选，明细行同步、去重、删除和默认值规则由应用服务编排，不能由组件直接写数据库。

注意：当前 `FieldTypeEnum.BUSINESS_MULTI` 与 `BUSINESS` 使用同一个类型值 `16`。因此多选不能依赖枚举数值区分，必须通过 `businessSelectConfig.multiple = true` 表达交互语义；新增实现时不要擅自修改已有枚举值或协议。

## 4. 前端实现规范

### 4.1 字段分发

动态表单应通过 `resolveFieldKind` 分发：

| 后端 `fieldType` | 前端类型 | 必须存在的配置 |
| --- | --- | --- |
| `12` | `business-select` | `businessSelectConfig` |
| `50` | `product-select` | `productSelectConfig` |
| `16` | `business-select` | `businessSelectConfig`，并按 `multiple` 判断多选 |

`DynamicFormField.vue` 负责传递字段值、禁用状态和选择事件；字段名称不能作为判断成员或产品的依据。

### 4.2 USER 渲染

`BusinessDataSelectField.vue` 使用后端下发的 `businessSelectConfig`：

1. 输入关键字后调用 `quickSearchUrl` 获取候选。
2. 打开弹窗后调用 `dialogSearchUrl` 获取分页候选和动态表头。
3. 已有 ID 或编辑页初始化时调用 `getByIdUrl` 回显 `label`、`name` 或 `code`。
4. 单选时向表单写入 `option.id`，同时按字段约定回填对应的 `Label` 展示字段。
5. 多选时通过 `selected-multiple` 事件交给业务明细或表单编排层处理。

请求必须合并 `requestPayload`，不能丢失 `corpid`。使用通用业务选择器时，优先使用后端返回的完整 URL；只有已有 Provider 注册约定时才使用 `businessSelectRegistry.ts` 的 Provider。

### 4.3 PRODUCT 渲染

`ProductDataSelectField.vue` 使用 `productSelectApi.ts` 的固定接口地址：

1. 快捷搜索调用产品 `quickSearch`。
2. 弹窗搜索调用产品 `dialogSearch`，按 `headList` 动态渲染表格列。
3. 编辑页按 `productSelectConfig.requestPayload + id` 调用 `getById` 回显。
4. 单选写入 `option.id`；多选通过 `selected-multiple` 交给业务明细行处理。
5. `linePatch` 只通过明确的字段映射回填，产品选择器不能自行决定业务默认值。

前端请求统一经项目请求封装；新增代码不得创建独立 Axios 实例或新增 `fetch`。若当前参考实现仍存在直接请求代码，新增功能应遵循前端 `AGENTS.md` 和 `docs/前端技术规约.md`，并在交付报告中注明兼容范围。

### 4.4 空值、禁用和回显

- 空值统一使用 `null` 或业务约定的空字符串，不用展示文本替代空值。
- `editable !== 1` 时选择器、清空按钮和弹窗入口必须不可操作。
- 回显失败不能静默显示旧值；请求错误应进入统一错误处理。
- 单选字段清空后发送 `null`，由后端按保存场景决定是否允许清空。
- 多选字段不能把数组直接塞入单选 ID 字段；应由明细行或多选字段专用数据结构承载。

## 5. Harness 交付流程

### 5.1 仅新增字段元数据

1. 在目标模块声明字段，选择正确的 `FieldTypeEnum`。
2. 为 `USER` 配置 `BusinessSelectConfig`；为 `PRODUCT` 配置 `ProductSelectConfig`。
3. 增加字段工厂/Assembler 单元测试，断言 `fieldType`、配置类型、`multiple`、`businessCode` 和上下文。
4. 增加前端字段分发或组件测试，断言 `12`、`50`、`16` 分别进入正确渲染分支。
5. 执行后端 Harness 校验和目标模块测试；前端执行 lint、typecheck 和受影响测试。

### 5.2 新增或修改选择接口

1. 先补充或更新 `docs/api/endpoints/*.md` 原子文档。
2. 若 URL、请求 DTO、响应 VO 或空值语义变化，执行 `.claude/commands/multi-player/SKILL.md` 要求的接口文档编排，并同步 `docs/kn/总目录.md`。
3. 后端按 Controller → Application → Domain → Repository 分层实现，禁止 Controller 直接调用 Mapper。
4. 前端补充类型、请求调用、加载/空态/错误态和回显测试。
5. 交付报告记录后端与前端仓库的变更文件、验证命令和未验证项。

## 6. 验收矩阵

| 检查项 | USER | PRODUCT |
| --- | --- | --- |
| `fieldType` | `12` | `50` |
| 配置对象 | `businessSelectConfig` | `productSelectConfig` |
| 主值 | 员工字符串 ID | 产品/SKU 稳定 ID |
| 快捷搜索 | 组织成员接口 | 产品接口常量 |
| 弹窗搜索 | 组织成员接口 | 产品接口常量 |
| 按 ID 回显 | `corpid + id` | 请求上下文 + `id` |
| 多选语义 | 仅在明确使用多选配置时启用 | `productSelectConfig.multiple` |
| 权限/状态 | 启用且在职 | 产品领域状态和业务条件 |
| 错误处理 | `BizException` + 统一响应 | `BizException` + 统一响应 |

交付前至少确认：

- 后端字段元数据没有把 `PRODUCT` 配置成 `BUSINESS`。
- `USER` 的 `corpid` 没有从请求上下文丢失，员工 ID 没有被错误转为数字。
- `PRODUCT` 没有从前端输入读取接口 URL，`businessCode` 与单据场景一致。
- 快捷搜索、弹窗搜索、按 ID 回显均有对应测试或接口验证证据。
- 多选选择结果由业务层处理，组件没有绕过保存服务写入业务数据。

## 7. 验证命令

后端仓库：

```bash
scripts/harness-verify.sh
mvn -pl xbb-erp-base-common -am test
```

如只影响具体业务模块，再补充目标模块测试，例如：

```bash
mvn -pl xbb-erp-module-product -am test
mvn -pl xbb-erp-module-org -am test
```

前端仓库：

```bash
pnpm lint
pnpm typecheck
pnpm --filter admin-web test -- --run
```

命令需根据实际受影响包调整；若既有工作区改动导致全量校验失败，应记录失败原因，不得通过重置或覆盖用户改动规避问题。

## 8. 相关文档

- `docs/kn/field-business-select-m.md`
- `docs/harness/前后端契约规范.md`
- `docs/harness/验证矩阵.md`
- `docs/api/endpoints/common-filter.md`
- `docs/api/endpoints/common-header.md`
- `docs/api/endpoints/org-quick-search.md`
- `docs/api/endpoints/org-dialog-search.md`
- `docs/api/endpoints/org-get-by-id.md`
- `docs/api/endpoints/product-business-select-quick-search.md`
- `docs/api/endpoints/product-business-select-dialog-search.md`
- `docs/api/endpoints/product-business-select-get-by-id.md`
