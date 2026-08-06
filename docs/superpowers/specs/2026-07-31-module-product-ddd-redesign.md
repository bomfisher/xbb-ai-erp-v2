# module-product 按 module-customer 最新 DDD 架构改造设计

## 1. 背景与目标

本次改造目标是让 `xbb-erp-module-product` 对齐 `xbb-erp-module-customer` 的最新 DDD 架构与接口协议规范，同时保留商品领域自身的业务特殊性。

本次范围明确如下：

- 覆盖 `module-product` 目录下现有全部接口
- 对外接口协议允许整体调整
- `product` 主档改造成类似 `customer` 的保存协议
- `warehouse` 保持普通 CRUD
- `brand`、`category`、`unit` 保持普通 CRUD
- 仅做后端改造，不联动前端仓库
- 数据库表结构仅允许最小必要调整，并在实现中明确列出

## 2. 设计原则

- 严格对齐项目 DDD 分层规范
- 接口统一使用 `POST + @RequestBody`
- 非脚本接口 DTO 继承 `BaseDTO`
- 所有接口返回统一使用 `ResultVO.success()` 包装
- 所有主动抛错统一使用 `BizException`
- 数据库直连对象统一使用 `PO` 后缀
- 布尔语义字段继续使用 `Integer`
- 避免把简单主数据过度设计成草稿协议
- 商品主档必须兼顾多规格场景，不能照搬单主档模型

## 3. 目标模块划分

`module-product` 改造后拆成两类能力：

### 3.1 商品主档聚合

商品主档聚合由以下对象组成：

- `ProductSpu`：商品主档公共信息
- `ProductSku`：商品规格明细行

该聚合在保存、详情、草稿、删除等场景中以整单方式处理。

### 3.2 基础主数据子域

以下子域保持普通 CRUD：

- `ProductCategory`
- `ProductBrand`
- `ProductUnit`
- `Warehouse`

其中 `Warehouse` 维持独立管理，不纳入商品聚合保存协议。

## 4. 目录结构设计

`module-product` 目标结构对齐 `module-customer`：

- `admin`
  - Controller
  - DTO
  - VO
  - 必要枚举
- `application`
  - `service/product/query`
  - `service/product/save`
  - `service/product/draft`
  - `service/product/delete`
  - `service/brand`
  - `service/category`
  - `service/unit`
  - `service/warehouse`
  - `assembler`
  - `validator`
  - `port`
  - `pojo`
  - `provider`（仅在存在明确元数据提供场景时引入）
  - `schema`（仅在存在明确列表动态结构场景时引入）
- `domain`
  - `model`
  - `repository`
  - `pojo`
- `infrastructure`
  - `persistence/po`
  - `persistence/mapper`
  - `persistence/repository`
  - `persistence/convertor`

本次不会为了形式对齐而强行引入 `field` 工厂等结构，只有在 `product` 出现和 `customer` 同等复杂的动态字段场景时才增加。

## 5. 商品主档接口协议设计

`product` 主档对齐 `customer` 的协议外形，但其语义调整为 `SPU + SKU[]` 聚合协议。

### 5.1 商品主档接口

建议接口如下：

- `POST /erp/v1/product/list`
- `POST /erp/v1/product/addItem`
- `POST /erp/v1/product/updateItem`
- `POST /erp/v1/product/saveDraft`
- `POST /erp/v1/product/saveAndSubmit`
- `POST /erp/v1/product/draftList`
- `POST /erp/v1/product/loadDraft`
- `POST /erp/v1/product/detail`
- `POST /erp/v1/product/delete`

### 5.2 保存协议结构

商品保存 DTO 不再沿用当前“SPU 字段 + 单 SKU 字段平铺”的结构，而改成聚合提交：

- `main`：商品主档 DTO
- `skus`：`List<ProductSkuItemDTO>`
- `draftMeta`：草稿元信息
- `sectionState`：本期默认不作为强制能力，仅在后续确认商品页存在与 `customer` 类似的区块开关需求时启用

### 5.3 返回结构

`detail` 与 `loadDraft` 返回聚合明细：

- `main`
- `skus`
- `draftMeta`（有草稿上下文时返回）

### 5.4 聚合语义

- 删除商品时按 `SPU` 维度删除整张商品及其全部 `SKU`
- 单条 `SKU` 的删除不提供独立删除接口
- 单条 `SKU` 的新增、修改、删除统一走整单保存同步

## 6. 多规格设计约束

`product` 作为特殊业务，需要兼顾单规格与多规格场景。

### 6.1 单规格场景

当 `enableSpec = 0` 时：

- 必须存在且仅存在 1 条 `SKU`
- 前端即使传入多条 `SKU`，后端也视为协议不合法

### 6.2 多规格场景

当 `enableSpec = 1` 时：

- 至少存在 1 条 `SKU`
- 允许存在多条 `SKU`
- 同一商品下 `skuCode` 不允许重复
- 同一商品下规格签名 `specSignature` 不允许重复

### 6.3 规格数据承载

- `SPU` 承载公共属性
- `SKU` 承载规格差异、条码、上下架、业务可用性等差异属性
- 当前阶段不扩展完整规格模板/规格值中心模型
- 若数据库中已有 `specSignature`、`specSnapshot` 字段，则直接复用
- 若缺失，则仅补充多规格保存所必需的最小字段

## 7. 应用层拆分设计

### 7.1 商品主档应用服务

参考 `module-customer` 的拆分方式，商品主档拆分为：

- `ProductAdminAppService`
- `ProductQueryAppService`
- `ProductSaveAppService`
- `ProductDraftAppService`
- `ProductDeleteAppService`

#### 职责划分

- `ProductAdminAppService`：为控制器提供统一门面
- `ProductQueryAppService`：处理 `list/detail`
- `ProductSaveAppService`：处理整单保存与提交
- `ProductDraftAppService`：处理草稿保存、草稿列表、草稿加载
- `ProductDeleteAppService`：处理整单删除

### 7.2 基础主数据应用服务

以下子域维持普通应用服务：

- `ProductBrandAppService`
- `ProductCategoryAppService`
- `ProductUnitAppService`
- `WarehouseAdminAppService` 或按统一命名迁移为 `WarehouseAppService`

它们不进入草稿协议，仅统一分层与接口风格。

## 8. 商品保存同步策略

商品主档保存链路采用“先校验、再保存 SPU、再同步 SKU”的策略。

### 8.1 执行顺序

1. 协议校验
2. 通用字段校验
3. 业务校验
4. 保存 `SPU`
5. 同步 `SKU[]`
6. 提交成功后清理草稿

### 8.2 SPU 保存策略

- 新增时先插入 `SPU` 并拿到 `spuId`
- 编辑时更新既有 `SPU`

### 8.3 SKU 同步策略

- 查询当前 `spuId` 下现有全部 `SKU`
- 以提交报文中的 `sku.id` 集合与现有数据做比对
- 提交中不存在的旧 `SKU`：删除
- 提交中存在且有 `id` 的 `SKU`：更新
- 提交中不存在 `id` 的 `SKU`：新增

### 8.4 删除策略

- 删除商品时先删全部 `SKU`
- 再删 `SPU`
- 不保留旧版“删除一个 `skuId` 时顺带删 `spu`”的混合语义

## 9. 校验器设计

与 `customer` 保持一致，分三层校验：

### 9.1 协议校验

- `main` 不为空
- `skus` 不为空
- 单规格数量合法
- 多规格数量合法
- 删除参数合法

### 9.2 通用校验

- 状态字段合法
- `Integer` 枚举位合法
- 必填字段完整
- 请求体结构完整

### 9.3 业务校验

- `spuCode` 唯一
- `skuCode` 唯一
- 同商品下 `specSignature` 唯一
- 分类、品牌、单位引用存在
- 必要时校验默认 SKU 唯一

所有校验失败统一抛出 `BizException`。

## 10. 草稿机制设计

商品草稿机制直接对齐 `customer` 的 Redis 草稿方案。

### 10.1 端口定义

新增应用层端口：

- `ProductDraftRepository`

### 10.2 存储策略

- 使用 `Redis` 保存草稿
- 草稿 Key 前缀：`product:draft:`
- 草稿索引 Key 前缀：`product:draft:index:`
- 设置草稿 TTL
- 设置单企业最大草稿数
- 超出上限时按最旧草稿淘汰

### 10.3 草稿内容

草稿内容保存：

- `main`
- `skus`
- `draftMeta`
- 必要的时间与标题信息

### 10.4 提交后的清理

- `saveAndSubmit` 成功后，若传入草稿编码，则删除对应草稿

## 11. 数据库与迁移策略

本次数据库调整遵循“最小必要原则”。

### 11.1 不调整的部分

- 不为了草稿引入 MySQL 草稿表
- 不为了未来规格中心能力一次性补全复杂表结构
- 不做与本次改造无关的表重构

### 11.2 可能需要调整的部分

仅在数据库实际缺失时补充以下多规格必要字段：

- `product_sku.spec_signature`
- `product_sku.spec_snapshot`

若线上/本地现有表已具备上述字段，则不新增 Flyway。

## 12. 普通 CRUD 子域改造边界

### 12.1 Warehouse

`warehouse` 已接近目标风格，但仍需统一：

- 命名与目录落位
- 测试结构
- DTO/VO 规范
- 异常语义

`warehouse` 保持以下协议形态：

- `list`
- `addItem`
- `updateItem`
- `save`
- `detail`
- `delete`

### 12.2 Brand / Category / Unit

这三个子域统一改造成普通 CRUD，不引入草稿协议。

统一要求：

- `POST + @RequestBody`
- DTO 继承 `BaseDTO`
- 列表使用 `ListBaseVO`
- 保存与删除返回风格统一
- 迁移到 `application/service`

## 13. 测试策略

测试层次对齐 `customer`：

- 结构测试
- 控制器协议结构测试
- 应用服务测试
- 仓储测试
- SQL 规划测试

### 13.1 商品主档关键测试

至少覆盖：

- 单规格保存成功
- 多规格保存成功
- 多规格下重复 `skuCode` 拒绝
- 多规格下重复 `specSignature` 拒绝
- 编辑时新增 `SKU`
- 编辑时更新 `SKU`
- 编辑时删除旧 `SKU`
- 草稿保存成功
- 草稿加载成功
- 提交后草稿清理成功
- 删除商品时级联删除 `SKU`

### 13.2 基础主数据测试

至少覆盖：

- `brand/category/unit/warehouse` 控制器结构测试
- 常规保存、详情、列表、删除应用服务测试
- 必要的仓储测试与条件映射测试

## 14. 实施顺序

建议实施顺序如下：

1. 重构 `admin` 协议与 DTO/VO
2. 重构 `application/service`、`assembler`、`validator`、`port`
3. 改造 `product` 聚合保存与草稿链路
4. 改造 `brand/category/unit/warehouse` 普通 CRUD 链路
5. 补充测试
6. 仅在缺字段时补充最小 Flyway

## 15. 非目标

本次明确不包含：

- 前端联动改造
- 完整规格中心建设
- 商品属性中心建设
- 库存业务、采购业务、销售业务联动
- 与本次需求无关的表结构重做

## 16. 结论

本次 `module-product` 改造将整体对齐 `module-customer` 最新 DDD 架构，但不会机械复用其业务模型。

核心策略如下：

- `product` 按 `SPU + SKU[]` 聚合改造成保存协议
- `brand/category/unit/warehouse` 保持普通 CRUD
- 草稿使用 Redis 保存，不新增 MySQL 草稿表
- 多规格只补最小必要能力，不扩展超范围模型
- 所有接口、异常、分层、测试风格统一到当前项目规范
