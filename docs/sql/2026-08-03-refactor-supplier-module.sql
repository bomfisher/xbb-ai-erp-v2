-- 供应商模块重构迁移 SQL
-- 生成日期：2026-08-03
-- 说明：将 supplier 模块数据库对象从 vendor 语义迁移到 supplier 最终态

RENAME TABLE `vendor` TO `supplier`;
RENAME TABLE `vendor_contact` TO `supplier_contact`;
RENAME TABLE `vendor_address` TO `supplier_address`;
RENAME TABLE `vendor_bank_account` TO `supplier_bank_account`;
RENAME TABLE `vendor_invoice_profile` TO `supplier_invoice_profile`;
RENAME TABLE `vendor_attachment_relation` TO `supplier_attachment_relation`;
RENAME TABLE `vendor_reference_summary` TO `supplier_reference_summary`;
RENAME TABLE `vendor_operate_log` TO `supplier_operate_log`;
RENAME TABLE `vendor_idempotent_record` TO `supplier_idempotent_record`;

ALTER TABLE `supplier` CHANGE COLUMN `vendor_code` `supplier_code` varchar(64) NOT NULL COMMENT '供应商编码', CHANGE COLUMN `vendor_name` `supplier_name` varchar(128) NOT NULL COMMENT '供应商名称', CHANGE COLUMN `vendor_short_name` `supplier_short_name` varchar(128) DEFAULT NULL COMMENT '供应商简称', CHANGE COLUMN `vendor_category` `supplier_category` varchar(32) NOT NULL COMMENT '供应商分类';

ALTER TABLE `supplier`
  DROP INDEX `uk_corpid_vendor_code`,
  DROP INDEX `uk_corpid_vendor_name`,
  ADD UNIQUE KEY `uk_corpid_supplier_code` (`corpid`, `supplier_code`),
  ADD UNIQUE KEY `uk_corpid_supplier_name` (`corpid`, `supplier_name`);

ALTER TABLE `supplier_contact` CHANGE COLUMN `vendor_id` `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID', ADD COLUMN `remark` varchar(500) DEFAULT NULL COMMENT '备注' AFTER `biz_status`;

ALTER TABLE `supplier_contact`
  DROP INDEX `idx_corpid_vendor_id`,
  DROP INDEX `idx_corpid_vendor_default_flag`,
  ADD KEY `idx_corpid_supplier_id` (`corpid`, `supplier_id`),
  ADD KEY `idx_corpid_supplier_default_flag` (`corpid`, `supplier_id`, `default_flag`);

ALTER TABLE `supplier_address`
  CHANGE COLUMN `vendor_id` `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID';

ALTER TABLE `supplier_address`
  DROP INDEX `idx_corpid_vendor_id`,
  DROP INDEX `idx_corpid_vendor_default_flag`,
  ADD KEY `idx_corpid_supplier_id` (`corpid`, `supplier_id`),
  ADD KEY `idx_corpid_supplier_default_flag` (`corpid`, `supplier_id`, `default_flag`);

ALTER TABLE `supplier_bank_account`
  CHANGE COLUMN `vendor_id` `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  ADD COLUMN `remark` varchar(500) DEFAULT NULL COMMENT '备注' AFTER `biz_status`;

ALTER TABLE `supplier_bank_account`
  DROP INDEX `idx_corpid_vendor_id`,
  DROP INDEX `idx_corpid_vendor_default_flag`,
  ADD KEY `idx_corpid_supplier_id` (`corpid`, `supplier_id`),
  ADD KEY `idx_corpid_supplier_default_flag` (`corpid`, `supplier_id`, `default_flag`);

ALTER TABLE `supplier_invoice_profile`
  CHANGE COLUMN `vendor_id` `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID',
  CHANGE COLUMN `registered_phone` `address_phone` varchar(64) DEFAULT NULL COMMENT '地址电话',
  ADD COLUMN `remark` varchar(500) DEFAULT NULL COMMENT '备注' AFTER `biz_status`,
  DROP COLUMN `registered_address`;

ALTER TABLE `supplier_invoice_profile`
  DROP INDEX `idx_corpid_vendor_id`,
  DROP INDEX `idx_corpid_vendor_default_flag`,
  ADD KEY `idx_corpid_supplier_id` (`corpid`, `supplier_id`),
  ADD KEY `idx_corpid_supplier_default_flag` (`corpid`, `supplier_id`, `default_flag`);

ALTER TABLE `supplier_attachment_relation`
  CHANGE COLUMN `vendor_id` `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID';

ALTER TABLE `supplier_attachment_relation`
  DROP INDEX `idx_corpid_vendor_id`,
  ADD KEY `idx_corpid_supplier_id` (`corpid`, `supplier_id`);

ALTER TABLE `supplier_reference_summary`
  CHANGE COLUMN `vendor_id` `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID';

ALTER TABLE `supplier_reference_summary`
  DROP INDEX `uk_corpid_vendor_id`,
  ADD UNIQUE KEY `uk_corpid_supplier_id` (`corpid`, `supplier_id`);

ALTER TABLE `supplier_operate_log`
  CHANGE COLUMN `vendor_id` `supplier_id` bigint(20) NOT NULL COMMENT '供应商ID';

ALTER TABLE `supplier_operate_log`
  DROP INDEX `idx_corpid_vendor_id`,
  ADD KEY `idx_corpid_supplier_id` (`corpid`, `supplier_id`);
