CREATE TABLE sys_business_config (
    id BIGINT NOT NULL AUTO_INCREMENT,
    corpid VARCHAR(64) NOT NULL COMMENT '租户标识',
    business_code VARCHAR(64) NOT NULL COMMENT '业务单据编码',
    config_json JSON NOT NULL COMMENT '非默认配置值对象，键为配置项编码',
    del TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记：0否，1是',
    add_time BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    update_time BIGINT NOT NULL COMMENT '更新时间戳（毫秒）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_sys_business_config_corpid_code (corpid, business_code)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='租户业务系统配置';
