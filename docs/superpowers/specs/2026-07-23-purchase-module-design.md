# 采购模块首期 6 张核心表初始化设计

## 1. 目标

在现有 DDD + Spring Boot + MyBatis-Plus 工程下，新增采购业务模块 `xbb-erp-module-purchase`，首期仅初始化采购主链的 6 张核心表 CRUD 骨架，并保持与当前 `xbb-erp-codegen`、`xbb-erp-module-supplier`、`xbb-erp-module-product` 已有模式一致。

本次范围明确为：

- 新建模块：`xbb-erp-module-purchase`
- 模块编码：`purchase`
- 包根路径：`xbb.ai.erp.module.purchase`
- 初始化范围：6 张核心表
- 供应商主数据：复用 `xbb-erp-module-supplier`，采购模块不复制供应商主档表

## 2. 本次范围

### 2.1 首期 6 张核心表

1. `purchase_pending_task`
2. `purchase_request`
3. `purchase_request_item`
4. `purchase_order`
5. `purchase_order_item`
6. `purchase_source_relation`

### 2.2 明确不在本次范围

以下表本次不初始化，留待第二期：

- `purchase_receiving_notice`
- `purchase_receiving_notice_item`
- `purchase_inbound`
- `purchase_inbound_item`
- `purchase_return`
- `purchase_return_item`
- `purchase_change`
- `purchase_change_item`
- `purchase_settlement_plan`
- `purchase_doc_snapshot`
- `purchase_write_back_log`
- `purchase_idempotent_record`

### 2.3 业务边界

采购模块只承接采购域内的任务、申请、订单与来源关系数据。

供应商相关能力采用“引用 + 快照”模式：

- 采购单中保留 `vendor_id`
- 采购单中保留 `vendor_name_snapshot`
- 不在采购模块内部复制 `vendor`、`vendor_contact`、`vendor_address` 等供应商主数据表
- 如后续需要供应商下拉、详情回显等能力，应通过 `xbb-erp-module-supplier` 提供能力给采购模块调用

## 3. 推荐实现方案

本次采用方案 B：`codegen` 优先，6 张表按 6 个独立聚合分别维护 YAML 规格，然后通过 dry-run 与 generate 生成骨架，再补齐生成器暂未覆盖的查询与批量能力。

### 3.1 不采用手工复制 supplier 模块的原因

直接复制 `xbb-erp-module-supplier` 虽然能快速出结果，但会把供应商领域的聚合形状和命名直接带入采购域，不利于采购模块后续扩展。

### 3.2 不采用混合式局部手工实现的原因

如果部分表走生成器、部分表完全手工实现，会导致模块内部生成标准不一致。首期虽然只做 6 张表，但第二期还会继续扩展到收料、入库、退料、变更，统一规格更利于后续演进。

## 4. 模块目录与包结构设计

### 4.1 模块目录

新增模块目录：

- `xbb-erp-module-purchase`

### 4.2 Java 包根路径

- `xbb.ai.erp.module.purchase`

### 4.3 目录落位

首期生成与补齐后的代码落位如下：

- `admin`
  - `PurchasePendingTaskAdminController`
  - `PurchaseRequestAdminController`
  - `PurchaseRequestItemAdminController`
  - `PurchaseOrderAdminController`
  - `PurchaseOrderItemAdminController`
  - `PurchaseSourceRelationAdminController`
  - `dto`
  - `vo`
- `application/service`
  - 每个聚合的管理端应用服务接口与实现
- `application/assembler`
  - DTO / VO / 领域对象转换
- `domain/model`
  - 6 个聚合的领域对象
- `domain/repository`
  - 6 个仓储接口
- `infrastructure/persistence/po`
  - 6 张表对应数据库持久化对象
- `infrastructure/persistence/mapper`
  - Mapper 接口
- `infrastructure/persistence/repository`
  - 仓储实现
- `infrastructure/persistence/convertor`
  - 领域对象与实体对象转换
- `src/main/resources/mapper/purchase`
  - Mapper XML

### 4.4 初始化阶段建包原则

先仅按 `.claude/commands/init-module/module-demo.md` 与项目现有模块模式创建 package 结构；业务文件优先通过 `xbb-erp-codegen` 生成，不手工预铺一批业务类。

## 5. 聚合拆分设计

首期采用 6 个独立聚合、6 份独立 YAML。

### 5.1 聚合清单

- `PurchasePendingTask`
- `PurchaseRequest`
- `PurchaseRequestItem`
- `PurchaseOrder`
- `PurchaseOrderItem`
- `PurchaseSourceRelation`

### 5.2 采用独立 YAML 的原因

1. 当前生成器示例以单聚合为主，按单聚合维护规格风险最低。
2. 每张表都能独立 dry-run 与 generate，方便逐张修正字段与落位。
3. `purchase_source_relation` 结构明显区别于普通单头/单行表，单独维护更清晰。
4. 第二期新增表时，可以继续按同一规则追加 YAML，不需要回头拆分。

## 6. YAML 规格文件规划

### 6.1 存放位置

建议统一放到：

- `xbb-erp-codegen/src/main/resources/examples/purchase/`

### 6.2 文件命名

- `purchase-pending-task.yaml`
- `purchase-request.yaml`
- `purchase-request-item.yaml`
- `purchase-order.yaml`
- `purchase-order-item.yaml`
- `purchase-source-relation.yaml`

### 6.3 YAML 共同约束

每份 YAML 至少包含以下字段：

- `moduleCode: purchase`
- `moduleName`
- `packageBase: xbb.ai.erp.module.purchase`
- `pathStrategy: ddd-mybatis-plus`
- `aggregate.aggregateName`
- `aggregate.tableName`
- `aggregate.fields`
- `generate`

### 6.4 字段映射规则

所有 YAML 字段映射都必须遵守当前项目约束：

- 直连数据库的对象实体后缀使用 `Entity`
- 直连数据库字段不使用布尔值，改用 `Integer`
- `userId` 为 `String`
- 枚举类必须以 `Enum` 结尾
- 非脚本接口入参 DTO 统一继承 `BaseDTO`

对于文档中的 `tinyint` 语义字段，例如：

- `del`
- `sales_linked_flag`
- `period_locked_flag`

统一映射为 `Integer`，不映射为 `boolean` / `Boolean`。

## 7. 6 张表的生成与补齐策略

### 7.1 第一类：任务与申请链路

包含：

- `purchase_pending_task`
- `purchase_request`
- `purchase_request_item`

这 3 张表主要承接“待采购 → 采购申请”主链，生成后重点补齐：

- 基础 CRUD
- 条件列表查询
- 逻辑删除
- 批量插入

### 7.2 第二类：订单链路

包含：

- `purchase_order`
- `purchase_order_item`

这 2 张表承接“采购订单头/行”主链。除了基础 CRUD 外，需要特别关注：

- 供应商字段采用引用 + 快照模式
- 列表查询高频条件字段的保留
- 财务与执行摘要字段的完整映射

### 7.3 第三类：来源关系链路

包含：

- `purchase_source_relation`

这张表虽然本次仍然通过生成器建骨架，但生成后需要重点补齐：

- 来源与目标双向条件查询
- 数量字段精度控制
- 乐观锁字段 `version`
- 来源/目标场景下的列表与统计口径

## 8. 接口与应用层骨架设计

### 8.1 管理端接口风格

接口 URL 统一采用项目规范：

- `/erp/v1/purchase/pending-task`
- `/erp/v1/purchase/request`
- `/erp/v1/purchase/request-item`
- `/erp/v1/purchase/order`
- `/erp/v1/purchase/order-item`
- `/erp/v1/purchase/source-relation`

### 8.2 控制层职责

控制层只负责：

- 接收入参 DTO
- 调用应用服务
- 返回统一 VO 或分页结果

不在控制层编排采购业务流程。

### 8.3 DTO / VO 规则

- 管理端入参统一放在 `admin/dto`
- 管理端出参统一放在 `admin/vo`
- 非脚本接口 DTO 统一继承 `BaseDTO`
- DTO 中保留 `corpid` 与 `userId`

### 8.4 应用服务职责

首期应用服务只承接最小 CRUD 编排，不在本次引入：

- 审批流转
- 自动下推
- 幂等控制
- 摘要回写
- 与库存/应付/发票/付款的跨模块协作

## 9. 查询、删除与批量规则

以下规则属于 6 张表统一补齐规则。

### 9.1 批量插入

必须提供 `insertBatch`，并通过 XML 批量 SQL 实现，禁止使用 `for` 循环逐条插入。

### 9.2 逻辑删除

必须提供：

- `removeById`
- `removeBatchByIds`

两者都采用逻辑删除，统一更新：

- `del = 1`
- `update_time`
- `modify_id`

### 9.3 条件查询与统计

必须提供：

- `findByCondition`
- `count`

两者在 `mapper xml` 中共用同一个筛选条件片段，避免条件分叉。

### 9.4 分页与排序规则

`findByCondition` 必须支持：

- `offset`
- `pageSize`
- `groupByStr`
- `orderByStr`

若仅传 `pageSize`、未传 `offset`，则只限制返回条数为 `pageSize`。

### 9.5 租户字段要求

所有有 `corpid` 的表在查询与统计时都必须显式传入 `corpid` 作为筛选条件。

## 10. 与 supplier 模块的复用策略

本次只做“业务字段复用”，不做“供应商主档复制”。

### 10.1 允许复用的内容

- 供应商 ID 作为采购订单业务字段
- 供应商名称快照字段命名模式
- `supplier` 模块已验证过的 admin / application / domain / persistence 目录落位模式

### 10.2 不复用的内容

- 不复制 `vendor` 主表与子表
- 不在采购模块再生成一套供应商联系人、地址、银行账户、开票信息实体
- 不为了本次 CRUD 初始化额外增加 supplier 集成封装

### 10.3 后续扩展方式

如果后续需要采购单选择供应商、回显供应商详情，应由采购模块通过应用层或集成层调用 `xbb-erp-module-supplier` 对外能力，而不是把供应商表映射复制到采购模块内。

## 11. SQL 与文档产物

### 11.1 建表 SQL

输出文件：

- `docs/sql/2026-07-23-init-purchase-module.sql`

SQL 中至少包含首期 6 张表的建表语句、索引语句，以及与文档设计一致的通用字段。

### 11.2 导航文档回写

更新：

- `docs/base/项目顶部和底部module导航.md`

新增 `xbb-erp-module-purchase` 的模块导航说明，并注明：

- 功能定位：采购管理首期核心链路模块
- 当前范围：6 张核心表
- 不负责什么：收料、入库、退料、变更、回写、幂等等第二期能力

## 12. dry-run、生成与验证流程

### 12.1 先创建模块 package 结构

仅创建模块 package 结构，不预先手工写业务文件。

### 12.2 逐份 YAML 执行 dry-run

每个 YAML 都先执行：

`mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='dry-run <spec.yaml>'`

核对以下落位是否正确：

- `admin`
- `application`
- `domain`
- `infrastructure/persistence`
- `src/main/resources/mapper/purchase`

### 12.3 dry-run 确认后执行 generate

每个 YAML 再执行：

`mvn -pl xbb-erp-codegen -am exec:java -Dexec.mainClass=xbb.ai.erp.codegen.cli.CodegenCli -Dexec.args='generate <spec.yaml> .'`

### 12.4 生成后补齐

补齐以下生成器未完全覆盖的内容：

- 批量插入 SQL
- 逻辑删除 SQL
- 条件片段复用
- 分页/排序支持
- DTO 继承 `BaseDTO`
- 目录与命名对齐项目约束

### 12.5 编译验证

至少验证：

- `xbb-erp-codegen`
- `xbb-erp-module-purchase`

若聚合构建牵出无关失败，需要记录下来，但不顺手修 unrelated 问题。

## 13. 风险与约束

### 13.1 生成器与项目命名约束可能存在细节偏差

项目要求数据库直连对象后缀为 `Entity`，而现有示例中可能存在 `PO` 命名习惯。本次实现需要以当前项目明确规则为准，在生成后做定向校正。

### 13.2 订单头行与来源关系字段较多

`purchase_order` 与 `purchase_source_relation` 的字段规模和索引复杂度高于普通主数据表，YAML 书写需要更谨慎，尤其要保证：

- 金额、数量、税率精度正确
- 索引字段不漏项
- `version`、`del`、`corpid` 规则一致

### 13.3 本次仅为首期 CRUD 骨架

本次不承诺打通完整采购业务流程，只交付首期核心链路的可编译 CRUD 骨架与 SQL 产物，为第二期业务深化提供基础。

## 14. 结论

本次采购模块初始化将采用“模块先建骨架、6 份 YAML 独立生成、逐份 dry-run、逐份 generate、生成后统一补齐”的方式落地。

这样做能最大程度复用当前仓库内 `xbb-erp-codegen` 与已有业务模块的成熟模式，同时避免把 `supplier` 的主数据模型错误复制进采购领域。首期只聚焦 `待采购 → 申请 → 订单 → 来源关系` 6 张核心表，范围清晰，便于后续扩展到收料、入库与逆向链路。