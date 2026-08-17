ALTER TABLE `purchase_inbound_item`
    ADD COLUMN `warehouse_id` bigint(20) DEFAULT NULL COMMENT '入库仓库ID' AFTER `unit_name`,
    ADD KEY `idx_purchase_inbound_item_warehouse` (`corpid`, `warehouse_id`);
