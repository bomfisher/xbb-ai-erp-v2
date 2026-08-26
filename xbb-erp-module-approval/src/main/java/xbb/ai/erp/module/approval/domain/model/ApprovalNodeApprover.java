package xbb.ai.erp.module.approval.domain.model;

import lombok.Data;

@Data
public class ApprovalNodeApprover {
    private Long id;
    private Long flowNodeId;
    private ApprovalApproverType approverType;
    private String approverValue;
    private Integer superiorLevel;
}
