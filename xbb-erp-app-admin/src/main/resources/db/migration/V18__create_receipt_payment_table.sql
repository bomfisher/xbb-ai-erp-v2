CREATE TABLE `receipt_payment` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `corpid` varchar(64) NOT NULL COMMENT '租户ID',
  `receipt_id` bigint(20) NOT NULL COMMENT '收款单ID',
  `bank_account_id` bigint(20) NOT NULL COMMENT '资金账户ID',
  `payment_method` varchar(32) NOT NULL COMMENT '收款方式',
  `amount` decimal(18,2) NOT NULL COMMENT '收款金额',
  `handling_fee` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '手续费',
  `transaction_no` varchar(128) DEFAULT NULL COMMENT '交易号或票据号',
  `remark` varchar(255) DEFAULT NULL COMMENT '收款备注',
  `sort_no` int(11) NOT NULL DEFAULT '0' COMMENT '排序号',
  `del` tinyint(2) NOT NULL DEFAULT '0',
  `add_time` bigint(20) NOT NULL,
  `update_time` bigint(20) NOT NULL,
  `creator_id` varchar(64) NOT NULL,
  `modify_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`),
  KEY `idx_receipt_payment_receipt` (`corpid`, `receipt_id`, `del`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='客户收款明细';
