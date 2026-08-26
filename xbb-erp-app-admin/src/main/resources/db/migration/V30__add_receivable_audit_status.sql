ALTER TABLE `receivable`
    ADD COLUMN `audit_status` tinyint(2) NOT NULL DEFAULT '0' COMMENT '审核状态：0待审核，1审核中，2已审核，3已拒绝，4无需审核' AFTER `status`,
    ADD KEY `idx_receivable_audit_status` (`corpid`, `audit_status`);
