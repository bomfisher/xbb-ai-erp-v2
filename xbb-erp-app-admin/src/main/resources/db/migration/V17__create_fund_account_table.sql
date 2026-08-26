CREATE TABLE `fund_account` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '主键',
  `corpid` varchar(64) NOT NULL COMMENT '租户ID',
  `account_code` varchar(64) NOT NULL COMMENT '账户编码',
  `account_name` varchar(128) NOT NULL COMMENT '账户名称',
  `currency` varchar(16) NOT NULL DEFAULT 'CNY' COMMENT '币别',
  `bank_account_no` varchar(128) DEFAULT NULL COMMENT '银行账号',
  `account_holder` varchar(128) DEFAULT NULL COMMENT '开户名',
  `bank_name` varchar(128) DEFAULT NULL COMMENT '开户行',
  `account_type` varchar(32) NOT NULL COMMENT '账户类型',
  `default_flag` tinyint(2) NOT NULL DEFAULT '0' COMMENT '是否默认账户',
  `enabled` tinyint(2) NOT NULL DEFAULT '1' COMMENT '状态',
  `remark` varchar(255) DEFAULT NULL COMMENT '备注',
  `del` tinyint(2) NOT NULL DEFAULT '0', `add_time` bigint(20) NOT NULL, `update_time` bigint(20) NOT NULL,
  `creator_id` varchar(64) NOT NULL, `modify_id` varchar(64) NOT NULL,
  PRIMARY KEY (`id`), UNIQUE KEY `uk_fund_account_code` (`corpid`, `account_code`), KEY `idx_fund_account_enabled` (`corpid`, `enabled`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='资金账户主数据';
