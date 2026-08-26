CREATE TABLE `purchase_invoice_line` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `corpid` varchar(64) NOT NULL,
    `purchase_invoice_id` bigint(20) NOT NULL,
    `line_no` int(11) NOT NULL,
    `product_id` bigint(20) DEFAULT NULL,
    `product_name` varchar(255) NOT NULL,
    `specification` varchar(255) DEFAULT NULL,
    `unit_name` varchar(64) DEFAULT NULL,yi
    `quantity` decimal(18,6) NOT NULL,
    `unit_price` decimal(18,6) NOT NULL,
    `tax_rate` decimal(18,6) NOT NULL DEFAULT '0',
    `untaxed_amount` decimal(18,2) NOT NULL DEFAULT '0',
    `tax_amount` decimal(18,2) NOT NULL DEFAULT '0',
    `amount` decimal(18,2) NOT NULL DEFAULT '0',
    `remark` varchar(255) DEFAULT NULL,
    `creator_id` varchar(64) NOT NULL,
    `modify_id` varchar(64) NOT NULL,
    `del` tinyint(2) NOT NULL DEFAULT '0',
    `add_time` bigint(20) NOT NULL,
    `update_time` bigint(20) NOT NULL,
    PRIMARY KEY (`id`), KEY `idx_purchase_invoice_line_invoice` (`corpid`, `purchase_invoice_id`, `del`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购发票明细';

CREATE TABLE `purchase_invoice_line_source` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT,
    `corpid` varchar(64) NOT NULL,
    `purchase_invoice_line_id` bigint(20) NOT NULL,
    `source_type` varchar(32) NOT NULL,
    `source_id` bigint(20) NOT NULL,
    `source_line_id` bigint(20) NOT NULL,
    `quantity` decimal(18,6) NOT NULL,
    `untaxed_amount` decimal(18,2) NOT NULL DEFAULT '0',
    `tax_amount` decimal(18,2) NOT NULL DEFAULT '0',
    `amount` decimal(18,2) NOT NULL DEFAULT '0',
    `creator_id` varchar(64) NOT NULL,
    `modify_id` varchar(64) NOT NULL,
    `del` tinyint(2) NOT NULL DEFAULT '0',
    `add_time` bigint(20) NOT NULL,
    `update_time` bigint(20) NOT NULL,
    PRIMARY KEY (`id`), KEY `idx_purchase_invoice_line_source_line` (`corpid`, `purchase_invoice_line_id`, `del`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='采购发票明细来源';
