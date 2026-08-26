package xbb.ai.erp.module.approval.domain.model;

import lombok.Data;

import java.util.List;

@Data
public class ApprovalFlowNode {
    private Long id;
    private Long flowDefinitionId;
    private Integer nodeNo;
    private String nodeName;
    private ApprovalNodeMode approvalMode;
    private List<ApprovalNodeApprover> approvers;
}
