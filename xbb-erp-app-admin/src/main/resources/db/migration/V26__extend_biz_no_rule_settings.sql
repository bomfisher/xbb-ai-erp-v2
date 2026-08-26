ALTER TABLE sys_biz_no_rule
    ADD COLUMN include_date TINYINT NOT NULL DEFAULT 0 COMMENT '是否包含时间编码：0否，1是' AFTER prefix,
    ADD COLUMN suffix_length INT NOT NULL DEFAULT 5 COMMENT '自增后缀位数' AFTER include_date,
    ADD COLUMN serial_mode VARCHAR(32) NOT NULL DEFAULT 'CONTINUOUS' COMMENT '自增方式：CONTINUOUS连续，DAILY按日' AFTER suffix_length;

UPDATE sys_biz_no_rule
SET include_date = CASE WHEN rule_type = 'DOCUMENT' THEN 1 ELSE 0 END,
    serial_mode = CASE WHEN rule_type = 'DOCUMENT' THEN 'DAILY' ELSE 'CONTINUOUS' END;
