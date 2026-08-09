ALTER TABLE `demo_sub`
    ADD COLUMN `user_id` varchar(50) DEFAULT NULL COMMENT '成员ID' AFTER `name`,
    ADD COLUMN `department_id` bigint DEFAULT NULL COMMENT '部门ID' AFTER `user_id`,
    ADD KEY `idx_demo_sub_user` (`corpid`, `user_id`, `del`),
    ADD KEY `idx_demo_sub_department` (`corpid`, `department_id`, `del`);
