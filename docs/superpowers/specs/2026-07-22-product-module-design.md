# 商品模块最小初始化设计文档

## 1. 目标与范围

本次目标是在当前仓库中初始化 `xbb-erp-module-product`，为商品模块建立符合现有 DDD 约束的最小可演进骨架。

本次初始化范围仅包含以下 5 张表：

- `product_category`
- `product_brand`
- `product_unit`
- `product_spu`
- `product_sku`

本次同时执行两部分工作：

- `init-module`：建立模块骨架、持久化层、领域层、仓储层与建表 SQL
- `/init-crud-ctrl`：生成管理端基础 CRUD 控制器骨架

本次初始化粒度为“最小可用初始化”，即：

- 新建 `xbb-erp-module-product` 模块并接入父工程
- 生成 5 张表的建表 SQL
- 生成 5 张表对应的 PO、Mapper、Mapper XML、领域对象、仓储接口、仓储实现
- 生成管理端接口骨架
- 为商品主档提供最小聚合能力
- 回写 `docs/base/项目顶部和底部module导航.md`

本次不包含以下内容：

- `product_spec`
- `product_spec_value`
- `product_sku_spec_rel`
- `product_barcode`
- `product_sku_unit`
- `product_inventory_policy`
- `product_business_policy`
- `product_manufacture_attr`
- `product_relation`
- `product_extension`
- facade 对外能力
- 通用列表能力接入
- 复杂筛选 DSL
- 规格、多条码、多单位、库存策略等完整商品域能力

## 2. 设计原则

本次初始化遵循以下原则：

- 严格遵循仓库现有 DDD 分层与 `module-demo` 模板约束
- 优先保证模块边界正确，再追求功能完整
- 字典类主数据保持单表 CRUD 风格
- `SPU/SKU` 采用商品主档聚合编排，而不是两个完全割裂的对外业务入口
- 列表查询保持“最基础简单分页”，不接入通用列表能力
- 仅实现当前初始化需要的最小能力，不提前埋入完整商品模块复杂性

## 3. 模块定位与职责边界

### 3.1 模块定位

`xbb-erp-module-product` 是商品主数据模块，负责本期最小范围内的商品基础主档初始化能力。

### 3.2 本次负责范围

本次模块负责：

- 商品分类管理
- 商品品牌管理
- 商品单位管理
- 商品 `SPU` 主档管理
- 商品 `SKU` 主档管理
- 商品主档最小聚合查询与维护

### 3.3 本次不负责范围

本次模块不负责：

- 规格模板与规格组合
- 多条码
- 多单位与包装规格
- 库存控制策略
- 默认业务属性
- 制造入口属性
- 商品关系与扩展属性
- facade 查询与校验能力
- 下游采购、销售、库存、制造协同

## 4. 目录结构设计

模块按 `module-demo` 的最小落位方式建立目录结构。

建议核心目录如下：

- `src/main/java/xbb/ai/erp/module/product/admin`
- `src/main/java/xbb/ai/erp/module/product/admin/dto`
- `src/main/java/xbb/ai/erp/module/product/admin/vo`
- `src/main/java/xbb/ai/erp/module/product/app/service`
- `src/main/java/xbb/ai/erp/module/product/domain/model`
- `src/main/java/xbb/ai/erp/module/product/domain/repository`
- `src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/po`
- `src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/mapper`
- `src/main/java/xbb/ai/erp/module/product/infrastructure/persistence/repository`
- `src/main/resources/mapper/product`

其中职责如下：

- `admin`：管理端控制器与对外 DTO/VO
- `app/service`：应用层编排，尤其承接商品主档聚合用例
- `domain/model`：5 个领域对象
- `domain/repository`：5 个仓储接口
- `infrastructure/persistence/po`：5 个持久化对象
- `infrastructure/persistence/mapper`：5 个 Mapper 接口
- `infrastructure/persistence/repository`：仓储实现
- `mapper/product`：Mapper XML

## 5. 数据建模边界

### 5.1 单表管理对象

以下 3 张表按字典型单表对象处理：

- `product_category`
- `product_brand`
- `product_unit`

这些对象对外提供标准单表 CRUD 与简单分页列表。

### 5.2 商品主档聚合对象

以下 2 张表在持久化层分开建模，在应用层按商品主档聚合编排：

- `product_spu`
- `product_sku`

聚合边界如下：

- 新建商品：先创建 `SPU`，再创建一个 `SKU`
- 修改商品：联动更新 `SPU` 与 `SKU`
- 删除商品：按商品主档语义处理 `SPU` 与其下 `SKU` 的逻辑删除
- 商品详情：聚合返回 `SPU + SKU`

本次最小版本固定为一个 `SPU` 对应一个 `SKU`，不引入多规格与多 `SKU` 派生模型。

## 6. 接口设计

### 6.1 字典类接口

以下 3 组接口各自提供基础单表 CRUD：

- `ProductCategoryAdminController`
- `ProductBrandAdminController`
- `ProductUnitAdminController`

每组接口提供：

- 新增
- 修改
- 删除
- 详情
- 分页列表

### 6.2 商品主档聚合接口

商品主档统一由 `ProductAdminController` 对外提供能力，而不拆成两套完全独立的 `SPU` / `SKU` 控制器。

本次提供以下接口：

- 商品新增
- 商品修改
- 商品删除
- 商品详情
- `SPU` 列表
- `SKU` 列表
- `SPU+SKU` 列表

### 6.3 三个列表接口的语义

#### `SPU` 列表

- 查询维度：`product_spu`
- 适用场景：商品主档视角管理
- 返回内容：`SPU` 主档核心字段

#### `SKU` 列表

- 查询维度：`product_sku`
- 适用场景：交易单元视角管理
- 返回内容：`SKU` 主档核心字段

#### `SPU+SKU` 列表

- 查询维度：以 `SKU` 为行，补齐所属 `SPU` 核心信息
- 适用场景：前台综合展示或主从联合查看
- 返回内容：`SKU` 字段 + 关联 `SPU` 关键字段

### 6.4 DTO / VO 约束

接口对象遵循项目统一规范：

- 入参统一使用 DTO，不使用散列参数
- 非脚本接口 DTO 统一继承 `BaseDTO`
- 出参统一使用 VO
- `userId` 使用字符串类型
- 持久化对象统一使用 `PO` 后缀
- 枚举统一使用 `Enum` 后缀

## 7. 列表与分页设计

本次列表能力保持最小实现，不接入 `xbb-erp-module-common` 的通用列表能力。

统一分页口径：

- 支持 `offset`
- 支持 `pageSize`
- 不引入通用 `groupByStr`
- 不引入通用 `orderByStr` 透传

分页策略如下：

- 字典表列表：单表分页
- `SPU` 列表：按 `product_spu` 单表分页
- `SKU` 列表：按 `product_sku` 单表分页
- `SPU+SKU` 列表：在 Mapper XML 中通过联表 SQL 完成分页查询

## 8. 持久化层设计

### 8.1 每张表生成内容

对 5 张表分别生成以下内容：

- `PO`
- `Mapper`
- `Mapper.xml`
- 领域对象
- 仓储接口
- 仓储实现

### 8.2 Mapper XML 通用能力

每张表的 `Mapper.xml` 至少提供：

- `insert`
- `insertBatch`
- `removeById`
- `removeBatchByIds`
- `update`
- `findById`
- `findByCondition`
- `count`

约束如下：

- `insertBatch` 必须使用 SQL 批量插入，不允许 `for` 循环插入
- 删除统一按逻辑删除实现
- 所有业务表均带 `corpid`
- `findByCondition` 与 `count` 共用同一筛选条件片段
- 简单分页通过 `offset` 与 `pageSize` 实现

### 8.3 聚合查询实现方式

`SPU/SKU` 聚合查询只在应用层与列表 SQL 中体现，不把基础仓储接口做成复杂查询平台。

即：

- `product_spu` 保持独立仓储
- `product_sku` 保持独立仓储
- 商品聚合编排由应用层负责
- `SPU+SKU` 联表列表在 Mapper XML 中单独提供查询 SQL

## 9. 建表 SQL 设计

建表 SQL 输出到：

- `docs/sql`

文件范围仅包含本次初始化的 5 张表，不提前加入后续附表。

SQL 设计应遵循数据库设计资料中的统一约束：

- 所有表包含 `id`
- 所有表包含 `corpid`
- 所有表包含 `del`
- 所有表包含 `add_time`
- 所有表包含 `update_time`
- 所有表包含 `creator_id`
- 所有表包含 `modify_id`
- `creator_id` / `modify_id` 使用 `varchar(50)`
- 数据库字段不使用布尔类型表达业务开关，统一使用整数型

## 10. 业务导航回写

初始化完成后，需要将 `xbb-erp-module-product` 回写到：

- `docs/base/项目顶部和底部module导航.md`

回写内容至少包含：

- 模块名称
- 功能定位
- 本次初始化范围
- 当前表范围
- 当前代码落位
- 明确未纳入本次范围的能力边界

## 11. 测试与验证边界

本次只做初始化级别的最小验证，不扩写完整商品业务测试体系。

最小验证要求如下：

- 父工程编译通过
- 新模块能被父工程正确聚合
- 新增 Mapper XML 不报加载错误
- 新增 DTO、VO、Controller、Repository 之间依赖关系正确
- 如存在清晰的现有测试模式，则为最关键基础类补最小测试

本次不要求完成以下验证：

- 完整商品领域规则测试
- 商品聚合集成测试
- 跨模块协同测试
- facade 对外契约测试

## 12. 交付边界总结

本次交付的本质不是“完整商品模块开发”，而是一次可持续演进的最小初始化。

本次交付重点是：

- 把 `xbb-erp-module-product` 的工程骨架建立正确
- 把 5 张核心主档表的持久化与仓储能力建立起来
- 让 `SPU/SKU` 以正确的商品主档聚合边界出现
- 让 `SPU`、`SKU`、`SPU+SKU` 三种列表能力提前成型
- 不把完整商品领域复杂度一次性压进初始化阶段
