ALTER TABLE purchase_order_item
    ADD COLUMN warehouse_id BIGINT(20) DEFAULT NULL COMMENT '采购入库仓库ID' AFTER unit_name,
    ADD KEY idx_purchase_order_item_warehouse (corpid, warehouse_id);
