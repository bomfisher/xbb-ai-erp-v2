# 出入库流水查询设计

## 范围与统一口径

本页展示每个 SKU 在一个仓库发生的一次入库或出库事件及该事件后的期末库存/成本；不展示锁库、解锁等不改变实存数量的操作。筛选与表头先固定在前端，首次进入不请求列表；用户点击查询后才调用后端列表接口。后续可增加后端元数据接口替换本地定义，但不得改变数据行或筛选语义。

时间口径暂以现有 `stock_transaction.occurred_at` 为准。完成历史插单设计后，统一改为 `effective_at`（业务生效时间），并保留 `posted_at`（实际过账时间）。

## 一单多产品的流水规则

一张入库或出库单包含多个产品明细时，**每个产品明细产生一条数量流水和一条对应成本流水**；列表一行对应一个“单据行 + SKU + 仓库”库存事件，不是单据表头一行。

```text
采购入库单 PI-001
  行 1：SKU-A，成品仓，10 件  -> 库存流水 1、成本流水 1
  行 2：SKU-B，成品仓，20 件  -> 库存流水 2、成本流水 2
```

当前记账已为命令的每个 `InboundLine`/`OutboundLine` 写入一条流水，并以 `idempotency_key = 单据幂等键 + sourceLineId` 关联数量和成本记录。后续必须将 `source_line_id` 显式落列，不能仅把它隐藏在幂等键字符串中；并增加 `event_id` 作为数量、成本、冲销与重算结果的稳定关联键。单据内同一 SKU + 仓库重复时，应在保存层先合并为一个业务明细，或允许多行并以 `source_line_id` 区分；库存服务不得再仅以 SKU + 仓库拒绝合法的不同单据行。

## 筛选项矩阵

| 筛选项 | 当前来源 | 是否可做 | 实现方式 | 缺口/改造 |
| --- | --- | --- | --- | --- |
| 单据日期范围 | `stock_transaction.occurred_at` | 可做 | `occurred_at >= start AND occurred_at < endExclusive`，在成本流水使用同一事件时间 | 插单后改用 `effective_at`；日期必须统一到业务时区 |
| 仓库分类 | 无 | 不可做 | 无法可靠推导 | `warehouse_category` + `warehouse.category_id` |
| 仓库 | `warehouse_id` | 可做 | 选择器传仓库 ID，按租户过滤 | 无 |
| 商品 | `sku_id` | 可做 | 选择器传 SKU ID | 无 |
| 审核状态 | 库存流水没有源单审核状态 | 仅能固定为“已入账” | 本页当前只记录已过账事件；不能展示源单“未审核/已审核” | 在事件快照 `source_audit_status`，或按来源类型关联源单；推荐快照 |
| 批次 | 无批次维度 | 不可做 | 无 | 批次主表及 `batch_id/batch_no` 加入余额、流水、成本与快照维度 |
| 商品类别 | `product_spu.category_name` | 可做 | 流水 JOIN SKU，再 JOIN SPU，按类别筛选 | 建议未来改为分类 ID/树 |
| 商品品牌 | 无 | 不可做 | 无 | 产品品牌字段或产品品牌关系表 |
| 商品标签 | 无 | 不可做 | 无 | 标签表 + SKU/SPU 标签关系 |
| 单据商品条码 | SKU 无条码 | 不可做 | 不能将 SKU 编码当条码 | `product_sku_barcode`（一 SKU 可多条码） |
| 规格型号 | `product_sku.specification` | 可做 | SKU 联表后 `LIKE`/精确筛选 | 无；结构化规格另立属性表 |
| 部门 | 库存事件无部门 | 不可做 | 无 | 事件快照 `department_id`，并明确取仓库部门、单据部门还是操作部门 |
| 单据类型 | `action_type` / `source_type` | 可做（受限） | 白名单映射入库、出库、采购入库、销售出库等稳定编码 | 推荐用 `business_code` 统一来源类型，避免页面依赖自由文本 |
| 辅助属性 | 无辅助属性维度 | 不可做 | 无 | 辅助属性定义与库存维度关联；同步改造所有库存表 |

## 表头矩阵

| 参考表头 | 当前来源 | 是否可做 | 实现方式/限制 |
| --- | --- | --- | --- |
| 商品编码、商品名称、规格型号、单位 | `product_sku` | 可做 | 流水按 `sku_id` 联表主数据；历史严格口径应保存 SKU 名称/单位快照。 |
| 单据类型 | `action_type`、`source_type` | 可做（受限） | 服务端用枚举显示名；当前数据不足以区分全部业务单据类型时需补 `business_code`。 |
| 单据编号 | 仅有 `source_id` | 不可做 | 不能把内部 ID 当单据编号；新增 `source_document_no` 快照。 |
| 单据日期 | `occurred_at` | 可做 | 当前显示过账/发生时间；未来显示业务生效日期。 |
| 审核状态 | 无 | 不可做真实状态 | 当前流水仅在确认记账后写入，可显示“已入账”；若需源单审核状态，存事件快照。 |
| 仓库 | `warehouse_id` 联 `warehouse` | 可做 | 历史严格口径需要仓库名称快照。 |
| 入库/出库数量 | `qty_change` | 可做 | `qty_change > 0` 放入入库数量，`qty_change < 0` 绝对值放入出库数量。 |
| 入库/出库单位成本、成本本位币 | `stock_cost_transaction` | 可做（受限） | 按同一 `idempotency_key` 关联；金额取 `total_cost_change`，出库显示绝对值。后续改按 `event_id`。 |
| 期末基本单位数量 | `stock_transaction.qty_after` | 可做（无插单时） | 追溯插单后，旧 `qty_after` 会失效，必须读取有效重算版本或快照。 |
| 平均成本、期末成本本位币 | `stock_cost_transaction.unit_cost_after/total_cost_after` | 可做（无插单时） | 同上；不能把当前余额金额回填到历史行。 |

## 查询实现

当前商品类别和规格通过主数据 contract 批量回填后过滤，单次最多拉取 10000 条流水再分页。大数据量场景应增加面向查询的 SKU 维度索引或冗余字段，避免应用层扫描。

后端使用库存流水为驱动表，按租户、日期、SKU、仓库、单据类型筛选，再关联 SKU/SPU/仓库及同事件成本流水。分页、排序和所有字段名由服务端白名单控制，前端不传 SQL、列名或来源表名。

当前可用的关联键是 `(corpid, idempotency_key)`；它在数量流水与成本流水中均为每产品行生成的相同值。迁移后改为 `event_id`，并对 `(corpid, effective_at, event_id)` 建索引。查询默认只显示已写入库存流水的事件，因此不会列出未审核、未确认或未入账的业务单据。

建议接口为 `POST /erp/v1/inventory/stockTransactionQuery/list`，请求包含日期范围、可做筛选项、分页参数；响应为参考表头所需行数据。筛选和表头可先在前端固定，后端后续提供 `/erp/v1/inventory/stockTransactionQuery/meta` 后再切换。

## 新增/演进字段与索引

为完整支持表头、审核状态、单据追溯和插单重算，后续新增：

- `stock_transaction`：`event_id`、`source_line_id`、`business_code`、`source_document_no`、`source_audit_status`、`effective_at`、`posted_at`；
- `stock_cost_transaction`：`event_id`、`calculation_version`；成本结果以版本化记录保存；
- 索引：数量流水 `(corpid, effective_at, warehouse_id, sku_id)`、`(corpid, business_code, source_id, source_line_id)`，成本流水 `(corpid, event_id, calculation_version)`；
- 若启用批次、辅助属性，必须把其 ID 加入余额唯一键、数量/成本流水、快照和重算任务，不可只加在页面筛选上。

## 验收

1. 一张两产品入库单返回两行流水，分别显示各自数量、入库金额及期末余额。
2. 一张单同一 SKU 不同仓库时返回两行；每行仓库和期末余额正确。
3. 日期、SKU、仓库、类别、规格、单据类型筛选均只影响服务端分页前结果。
4. 出库数量与出库成本金额按绝对值显示，库存变动方向不丢失。
5. 历史插单后，不允许继续使用旧行的 `qty_after` 和 `total_cost_after` 作为最终事实，必须等待重算版本发布。
