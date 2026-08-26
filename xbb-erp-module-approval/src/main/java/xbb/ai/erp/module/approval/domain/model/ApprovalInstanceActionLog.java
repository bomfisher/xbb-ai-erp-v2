package xbb.ai.erp.module.approval.domain.model;

import lombok.Data;

/**
 * 审批实例不可变操作记录。
 */
@Data
public class ApprovalInstanceActionLog {

    private Long id;

    private String instanceId;

    private Integer nodeNo;

    private String operatorId;

    private String action;

    private String comment;

    private Long operatedAt;
}
