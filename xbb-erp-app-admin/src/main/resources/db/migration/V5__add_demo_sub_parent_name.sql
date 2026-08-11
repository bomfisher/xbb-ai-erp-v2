ALTER TABLE `demo_sub`
    ADD COLUMN `parent_name` varchar(50) DEFAULT NULL COMMENT '关联DEMO名称' AFTER `name`;
