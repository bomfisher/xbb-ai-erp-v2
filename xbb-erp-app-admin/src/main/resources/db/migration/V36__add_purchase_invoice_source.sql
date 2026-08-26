ALTER TABLE `purchase_invoice`
    ADD COLUMN `source_type` varchar(32) DEFAULT NULL COMMENT '开票来源类型：采购入库、采购订单、手工录入' AFTER `remark`,
    ADD COLUMN `source_id` bigint(20) DEFAULT NULL COMMENT '来源单据ID' AFTER `source_type`,
    ADD COLUMN `manual_reason` varchar(255) DEFAULT NULL COMMENT '手工来源原因' AFTER `source_id`,
    ADD KEY `idx_purchase_invoice_source` (`corpid`, `source_type`, `source_id`);
