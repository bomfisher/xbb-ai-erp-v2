CREATE TABLE approval_flow_definition (
    id BIGINT NOT NULL AUTO_INCREMENT,
    corpid VARCHAR(64) NOT NULL COMMENT '租户标识',
    business_code VARCHAR(64) NOT NULL COMMENT '接入方业务对象编码',
    approval_scene VARCHAR(16) NOT NULL COMMENT '审批场景：CREATE、UPDATE',
    flow_code VARCHAR(64) NOT NULL COMMENT '流程稳定编码',
    flow_name VARCHAR(128) NOT NULL COMMENT '流程名称',
    version INT NOT NULL COMMENT '流程版本号',
    priority INT NOT NULL DEFAULT 100 COMMENT '匹配优先级，值越小优先级越高',
    status VARCHAR(16) NOT NULL COMMENT 'DRAFT、PUBLISHED、DISABLED、ARCHIVED',
    scope_json JSON NOT NULL COMMENT '发起人范围和字段条件',
    creator_id VARCHAR(64) NOT NULL COMMENT '创建人',
    modify_id VARCHAR(64) NOT NULL COMMENT '修改人',
    del TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    add_time BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    update_time BIGINT NOT NULL COMMENT '更新时间戳（毫秒）',
    PRIMARY KEY (id),
    UNIQUE KEY uk_approval_flow_version (corpid, business_code, approval_scene, flow_code, version),
    KEY idx_approval_flow_match (corpid, business_code, approval_scene, status, priority)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批流程定义';

CREATE TABLE approval_flow_node (
    id BIGINT NOT NULL AUTO_INCREMENT,
    flow_definition_id BIGINT NOT NULL COMMENT '流程定义标识',
    node_no INT NOT NULL COMMENT '节点序号，从 1 开始连续',
    node_name VARCHAR(128) NOT NULL COMMENT '节点名称',
    approval_mode VARCHAR(16) NOT NULL COMMENT 'ALL_SIGN、ANY_SIGN',
    del TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    add_time BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    update_time BIGINT NOT NULL COMMENT '更新时间戳（毫秒）',
    PRIMARY KEY (id),
    KEY idx_approval_flow_node (flow_definition_id, node_no, del)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批流程节点';

CREATE TABLE approval_node_approver (
    id BIGINT NOT NULL AUTO_INCREMENT,
    flow_node_id BIGINT NOT NULL COMMENT '流程节点标识',
    approver_type VARCHAR(16) NOT NULL COMMENT 'USER、ROLE、SUPERIOR',
    approver_value VARCHAR(128) NULL COMMENT '人员或角色标识',
    superior_level INT NULL COMMENT '主管层级',
    del TINYINT NOT NULL DEFAULT 0 COMMENT '删除标记',
    add_time BIGINT NOT NULL COMMENT '创建时间戳（毫秒）',
    update_time BIGINT NOT NULL COMMENT '更新时间戳（毫秒）',
    PRIMARY KEY (id),
    KEY idx_approval_node_approver (flow_node_id, del)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批节点审批人规则';
