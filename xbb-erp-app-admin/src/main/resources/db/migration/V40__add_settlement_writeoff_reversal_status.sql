ALTER TABLE receipt_writeoff
    ADD COLUMN status TINYINT(2) NOT NULL DEFAULT 1 COMMENT '核销状态：1有效，0已冲销' AFTER amount,
    ADD COLUMN reversed_time BIGINT(20) DEFAULT NULL COMMENT '冲销时间' AFTER status;

ALTER TABLE payment_writeoff
    ADD COLUMN status TINYINT(2) NOT NULL DEFAULT 1 COMMENT '核销状态：1有效，0已冲销' AFTER amount,
    ADD COLUMN reversed_time BIGINT(20) DEFAULT NULL COMMENT '冲销时间' AFTER status;
