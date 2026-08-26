CREATE TABLE `receipt_writeoff` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `corpid` varchar(64) NOT NULL COMMENT '租户ID',
    `receipt_id` bigint(20) NOT NULL COMMENT '收款单ID',
    `receivable_id` bigint(20) NOT NULL COMMENT '应收开放项ID',
    `writeoff_date` bigint(20) NOT NULL COMMENT '核销日期',
    `amount` decimal(18,2) NOT NULL COMMENT '核销金额',
    `creator_id` varchar(64) NOT NULL,
    `modify_id` varchar(64) NOT NULL,
    `del` tinyint(2) NOT NULL DEFAULT '0',
    `add_time` bigint(20) NOT NULL,
    `update_time` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_receipt_writeoff_no` (`corpid`, `writeoff_no`, `receipt_id`, `receivable_id`),
    KEY `idx_receipt_writeoff_receipt` (`corpid`, `receipt_id`),
    KEY `idx_receipt_writeoff_receivable` (`corpid`, `receivable_id`),
    KEY `idx_receipt_writeoff_date` (`corpid`, `writeoff_date`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户收款与应收核销记录';
