# 采购入库单行表设计

执行事实源为 `xbb-erp-app-admin/src/main/resources/db/migration/V9__create_purchase_inbound_item.sql` 及 `V11__add_purchase_inbound_item_warehouse.sql`。

`purchase_inbound_item` 保存采购入库单的产品行及采购价、成本价快照，按租户和 SKU 建立查询索引。

| 字段组 | 字段 | 说明 |
| --- | --- | --- |
| 归属 | `corpid`、`purchase_inbound_id`、`purchase_order_item_id` | 租户、入库单和来源采购订单行 |
| 产品 | `sku_id`、`sku_name`、`unit_name`、`warehouse_id` | 入库产品及名称、单位与实际入库仓库快照 |
| 数量金额 | `qty`、`unit_price`、`amount`、`cost_unit`、`cost_amount` | 入库数量、采购价、采购金额、成本单价和成本金额 |
| 审计 | `del`、`add_time`、`update_time`、`creator_id`、`modify_id` | 逻辑删除和审计字段 |
