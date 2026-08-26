ALTER TABLE `receipt_writeoff`
    ADD COLUMN `writeoff_no` varchar(64) NOT NULL COMMENT '核销批次号' AFTER `corpid`,
    ADD COLUMN `remark` varchar(255) DEFAULT NULL COMMENT '备注' AFTER `amount`,
    DROP COLUMN `status`,
    DROP COLUMN `reversed_time`,
    DROP INDEX `idx_receipt_writeoff_receipt`,
    DROP INDEX `idx_receipt_writeoff_receivable`,
    ADD UNIQUE KEY `uk_receipt_writeoff_no` (`corpid`, `writeoff_no`, `receipt_id`, `receivable_id`),
    ADD KEY `idx_receipt_writeoff_receipt` (`corpid`, `receipt_id`),
    ADD KEY `idx_receipt_writeoff_receivable` (`corpid`, `receivable_id`),
    ADD KEY `idx_receipt_writeoff_date` (`corpid`, `writeoff_date`);
