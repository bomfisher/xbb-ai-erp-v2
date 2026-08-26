CREATE TABLE `sales_invoice` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `corpid` varchar(64) NOT NULL COMMENT '租户ID',
    `invoice_no` varchar(64) NOT NULL COMMENT '销售发票编号',
    `customer_id` bigint(20) NOT NULL COMMENT '客户ID',
    `invoice_date` bigint(20) NOT NULL COMMENT '发票日期',
    `due_date` bigint(20) DEFAULT NULL COMMENT '应收到期日',
    `payment_term` varchar(64) DEFAULT NULL COMMENT '付款条件',
    `untaxed_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '未税金额',
    `tax_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '税额',
    `amount` decimal(18,2) NOT NULL COMMENT '含税总金额',
    `invoice_type` varchar(32) NOT NULL DEFAULT 'SALE' COMMENT '类型',
    `status` varchar(32) NOT NULL COMMENT '状态',
    `audit_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '审核状态：0待审核，1审核中，2已审核，3已拒绝',
    `audit_time` bigint(20) DEFAULT NULL COMMENT '审核或过账时间',
    `posted_time` bigint(20) DEFAULT NULL COMMENT '过账时间',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `del` tinyint(2) NOT NULL DEFAULT '0',
    `add_time` bigint(20) NOT NULL,
    `update_time` bigint(20) NOT NULL,
    `creator_id` varchar(64) NOT NULL,
    `modify_id` varchar(64) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sales_invoice_no` (`corpid`, `invoice_no`),
    KEY `idx_sales_invoice_customer_date` (`corpid`, `customer_id`, `invoice_date`),
    KEY `idx_sales_invoice_status_due_date` (`corpid`, `status`, `due_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售发票';

CREATE TABLE `sales_invoice_line` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `corpid` varchar(64) NOT NULL COMMENT '租户ID',
    `sales_invoice_id` bigint(20) NOT NULL COMMENT '销售发票ID',
    `line_no` int(11) NOT NULL COMMENT '行号',
    `product_id` bigint(20) DEFAULT NULL COMMENT '商品或服务ID',
    `product_name` varchar(255) NOT NULL COMMENT '商品或服务名称快照',
    `specification` varchar(255) DEFAULT NULL COMMENT '规格型号快照',
    `unit_id` bigint(20) DEFAULT NULL COMMENT '单位ID',
    `quantity` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '数量',
    `unit_price` decimal(18,6) NOT NULL DEFAULT '0.000000' COMMENT '未税单价',
    `tax_rate` decimal(8,4) NOT NULL DEFAULT '0.0000' COMMENT '税率',
    `untaxed_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '未税金额',
    `tax_amount` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '税额',
    `amount` decimal(18,2) NOT NULL COMMENT '含税金额',
    `remark` varchar(255) DEFAULT NULL COMMENT '备注',
    `del` tinyint(2) NOT NULL DEFAULT '0',
    `add_time` bigint(20) NOT NULL,
    `update_time` bigint(20) NOT NULL,
    `creator_id` varchar(64) NOT NULL,
    `modify_id` varchar(64) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_sales_invoice_line_no` (`corpid`, `sales_invoice_id`, `line_no`),
    KEY `idx_sales_invoice_line_invoice` (`corpid`, `sales_invoice_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售发票明细';

CREATE TABLE `sales_invoice_business_ref` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `corpid` varchar(64) NOT NULL COMMENT '租户ID',
    `sales_invoice_id` bigint(20) NOT NULL COMMENT '销售发票ID',
    `ref_type` varchar(32) NOT NULL COMMENT '归属类型',
    `ref_id` bigint(20) NOT NULL COMMENT '归属业务对象ID',
    `amount` decimal(18,2) DEFAULT NULL COMMENT '归属金额',
    `del` tinyint(2) NOT NULL DEFAULT '0', `add_time` bigint(20) NOT NULL, `update_time` bigint(20) NOT NULL,
    `creator_id` varchar(64) NOT NULL, `modify_id` varchar(64) NOT NULL,
    PRIMARY KEY (`id`), UNIQUE KEY `uk_sales_invoice_business_ref` (`corpid`, `sales_invoice_id`, `ref_type`, `ref_id`),
    KEY `idx_sales_invoice_business_ref_target` (`corpid`, `ref_type`, `ref_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售发票业务归属关系';

CREATE TABLE `sales_invoice_line_source` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键', `corpid` varchar(64) NOT NULL COMMENT '租户ID',
    `sales_invoice_line_id` bigint(20) NOT NULL COMMENT '销售发票明细ID', `source_type` varchar(32) NOT NULL COMMENT '来源类型',
    `source_id` bigint(20) DEFAULT NULL COMMENT '来源表头ID', `source_line_id` bigint(20) DEFAULT NULL COMMENT '来源明细ID',
    `quantity` decimal(18,6) NOT NULL DEFAULT '0.000000', `untaxed_amount` decimal(18,2) NOT NULL DEFAULT '0.00',
    `tax_amount` decimal(18,2) NOT NULL DEFAULT '0.00', `amount` decimal(18,2) NOT NULL DEFAULT '0.00',
    `del` tinyint(2) NOT NULL DEFAULT '0', `add_time` bigint(20) NOT NULL, `update_time` bigint(20) NOT NULL,
    `creator_id` varchar(64) NOT NULL, `modify_id` varchar(64) NOT NULL,
    PRIMARY KEY (`id`), KEY `idx_invoice_line_source_invoice_line` (`corpid`, `sales_invoice_line_id`),
    KEY `idx_invoice_line_source_source` (`corpid`, `source_type`, `source_id`, `source_line_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='销售发票明细来源及开票占用记录';
