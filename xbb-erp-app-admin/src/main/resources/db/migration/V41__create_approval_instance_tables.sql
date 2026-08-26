CREATE TABLE approval_instance (
    id BIGINT NOT NULL AUTO_INCREMENT,
    instance_id VARCHAR(64) NOT NULL COMMENT '审批实例标识',
    corpid VARCHAR(64) NOT NULL COMMENT '租户标识',
    flow_definition_id BIGINT NOT NULL COMMENT '冻结流程定义标识',
    flow_code VARCHAR(64) NOT NULL COMMENT '流程编码',
    flow_version INT NOT NULL COMMENT '流程版本',
    business_code VARCHAR(64) NOT NULL COMMENT '业务编码',
    approval_scene VARCHAR(16) NOT NULL COMMENT '审批场景',
    subject_id VARCHAR(64) NULL COMMENT '业务单据标识',
    request_id VARCHAR(128) NOT NULL COMMENT '提交幂等键',
    submitter_id VARCHAR(64) NOT NULL COMMENT '提交人',
    subject_summary VARCHAR(256) NOT NULL COMMENT '单据摘要',
    subject_snapshot_json JSON NOT NULL COMMENT '冻结业务快照',
    flow_snapshot_json JSON NOT NULL COMMENT '冻结流程快照',
    status VARCHAR(32) NOT NULL COMMENT '审批状态',
    current_node_no INT NULL COMMENT '当前审批节点序号',
    submitted_at BIGINT NOT NULL COMMENT '提交时间',
    completed_at BIGINT NULL COMMENT '完成时间',
    PRIMARY KEY (id),
    UNIQUE KEY uk_approval_instance_id (instance_id),
    UNIQUE KEY uk_approval_instance_request (corpid, request_id),
    KEY idx_approval_instance_subject (corpid, business_code, subject_id),
    KEY idx_approval_instance_flow_status (corpid, flow_definition_id, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批实例';

CREATE TABLE approval_instance_task (
    id BIGINT NOT NULL AUTO_INCREMENT,
    instance_id VARCHAR(64) NOT NULL COMMENT '审批实例标识',
    node_no INT NOT NULL COMMENT '审批节点序号',
    assignee_id VARCHAR(64) NOT NULL COMMENT '冻结审批人',
    status VARCHAR(16) NOT NULL COMMENT 'PENDING、APPROVED、REJECTED、CANCELLED',
    comment VARCHAR(512) NULL COMMENT '处理意见',
    handled_at BIGINT NULL COMMENT '处理时间',
    PRIMARY KEY (id),
    KEY idx_approval_task_assignee (assignee_id, status, instance_id),
    KEY idx_approval_task_instance_node (instance_id, node_no, status)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批实例任务';

CREATE TABLE approval_instance_action_log (
    id BIGINT NOT NULL AUTO_INCREMENT,
    instance_id VARCHAR(64) NOT NULL COMMENT '审批实例标识',
    node_no INT NULL COMMENT '审批节点序号',
    operator_id VARCHAR(64) NULL COMMENT '操作人',
    action VARCHAR(32) NOT NULL COMMENT 'SUBMIT、AUTO_APPROVE、APPROVE、REJECT、WITHDRAW',
    comment VARCHAR(512) NULL COMMENT '操作说明',
    operated_at BIGINT NOT NULL COMMENT '操作时间',
    PRIMARY KEY (id),
    KEY idx_approval_action_instance (instance_id, id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='审批实例操作日志';
