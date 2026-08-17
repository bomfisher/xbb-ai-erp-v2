CREATE TABLE sys_biz_no_rule (
    id BIGINT NOT NULL AUTO_INCREMENT,
    corpid VARCHAR(64) NOT NULL,
    business_code VARCHAR(64) NOT NULL,
    prefix VARCHAR(64) NOT NULL,
    rule_type VARCHAR(32) NOT NULL,
    del TINYINT NOT NULL DEFAULT 0,
    add_time BIGINT NOT NULL,
    update_time BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_sys_biz_no_rule_corpid_code (corpid, business_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务编号规则';

CREATE TABLE sys_biz_no_counter (
    id BIGINT NOT NULL AUTO_INCREMENT,
    corpid VARCHAR(64) NOT NULL,
    business_code VARCHAR(64) NOT NULL,
    period_key VARCHAR(16) NOT NULL,
    current_value BIGINT NOT NULL DEFAULT 0,
    add_time BIGINT NOT NULL,
    update_time BIGINT NOT NULL,
    PRIMARY KEY (id),
    UNIQUE KEY uk_sys_biz_no_counter_scope (corpid, business_code, period_key)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='业务编号持久化计数器';
