# 采购订单行表设计

执行事实源为 `xbb-erp-app-admin/src/main/resources/db/migration/V8__create_purchase_order_item.sql`。

`purchase_order_item` 是采购订单 `purchase_order` 的子表，按 `purchase_order_id + line_no` 唯一定位行，按 `corpid + sku_id` 支持租户内 SKU 查询。

| 字段组 | 字段 | 说明 |
| --- | --- | --- |
| 归属 | `corpid`、`purchase_order_id`、`line_no` | 租户、采购订单和行号 |
| SKU 快照 | `sku_id`、`sku_code`、`sku_name`、`specification`、`unit_name` | 订单行选择 SKU 时固定的业务快照 |
| 数量金额 | `qty`、`inbound_qty`、`unit_price`、`tax_rate`、`amount` | 下单、已入库、单价、税率和金额 |
| 审计 | `del`、`add_time`、`update_time`、`creator_id`、`modify_id` | 逻辑删除与审计字段，由基础实体和应用层维护 |
