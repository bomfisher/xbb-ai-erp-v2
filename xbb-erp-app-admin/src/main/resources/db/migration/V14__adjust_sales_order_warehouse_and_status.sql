UPDATE sales_order
SET status = '0'
WHERE status IS NULL OR status NOT REGEXP '^[0-2]$';

ALTER TABLE sales_order
    DROP COLUMN customer_name,
    MODIFY COLUMN warehouse_id bigint(20) DEFAULT NULL COMMENT '快捷选择仓库，不参与业务',
    MODIFY COLUMN status tinyint(2) NOT NULL DEFAULT '0' COMMENT '单据状态：0未关闭，1已关闭，2手动关闭';

ALTER TABLE sales_order_item
    ADD COLUMN warehouse_id bigint(20) DEFAULT NULL COMMENT '锁库仓库' AFTER sales_order_id,
    ADD KEY idx_sales_order_item_warehouse_sku (corpid, warehouse_id, sku_id);
