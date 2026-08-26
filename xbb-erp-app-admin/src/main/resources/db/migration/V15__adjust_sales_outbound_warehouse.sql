ALTER TABLE sales_outbound
    MODIFY COLUMN warehouse_id bigint(20) DEFAULT NULL COMMENT '快捷选择仓库，不参与业务';

ALTER TABLE sales_outbound_item
    ADD COLUMN warehouse_id bigint(20) DEFAULT NULL COMMENT '出库仓库ID' AFTER sku_name,
    ADD KEY idx_sales_outbound_item_warehouse_sku (corpid, warehouse_id, sku_id);
