ALTER TABLE approval_instance
    ADD COLUMN del TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记' AFTER completed_at,
    ADD COLUMN add_time BIGINT NOT NULL DEFAULT 0 COMMENT '创建时间' AFTER del,
    ADD COLUMN update_time BIGINT NOT NULL DEFAULT 0 COMMENT '更新时间' AFTER add_time;

ALTER TABLE approval_instance_task
    ADD COLUMN del TINYINT NOT NULL DEFAULT 0 COMMENT '逻辑删除标记' AFTER handled_at,
    ADD COLUMN add_time BIGINT NOT NULL DEFAULT 0 COMMENT '创建时间' AFTER del,
    ADD COLUMN update_time BIGINT NOT NULL DEFAULT 0 COMMENT '更新时间' AFTER add_time;
