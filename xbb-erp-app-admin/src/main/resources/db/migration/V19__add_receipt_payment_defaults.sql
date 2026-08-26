ALTER TABLE `receipt_payment`
    MODIFY COLUMN `handling_fee` decimal(18,2) NOT NULL DEFAULT '0.00' COMMENT '手续费',
    MODIFY COLUMN `transaction_no` varchar(128) NOT NULL DEFAULT '' COMMENT '交易号或票据号',
    MODIFY COLUMN `remark` varchar(255) NOT NULL DEFAULT '' COMMENT '收款备注';
