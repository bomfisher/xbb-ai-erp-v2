CREATE TABLE `payment_detail` (
    `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
    `corpid` varchar(64) NOT NULL COMMENT '租户ID',
    `payment_id` bigint(20) NOT NULL COMMENT '付款单ID',
    `bank_account_id` bigint(20) NOT NULL COMMENT '付款账户ID',
    `payment_method` varchar(32) NOT NULL COMMENT '付款方式',
    `amount` decimal(18,2) NOT NULL COMMENT '付款金额',
    `handling_fee` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '手续费',
    `transaction_no` varchar(128) DEFAULT NULL COMMENT '对接流水号',
    `remark` varchar(255) DEFAULT NULL COMMENT '付款备注',
    `sort_no` int(11) NOT NULL COMMENT '排序号',
    `creator_id` varchar(64) NOT NULL COMMENT '创建人',
    `modify_id` varchar(64) NOT NULL COMMENT '修改人',
    `del` tinyint(2) NOT NULL DEFAULT '0',
    `add_time` bigint(20) NOT NULL,
    `update_time` bigint(20) NOT NULL,
    PRIMARY KEY (`id`),
    KEY `idx_payment_detail_payment` (`corpid`, `payment_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='付款明细';
