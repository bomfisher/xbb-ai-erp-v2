ALTER TABLE `sales_invoice`
    ADD COLUMN `original_invoice_id` bigint(20) DEFAULT NULL COMMENT '红冲对应的原销售发票ID' AFTER `invoice_type`,
    ADD UNIQUE KEY `uk_sales_invoice_original` (`corpid`, `original_invoice_id`);
