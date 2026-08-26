package xbb.ai.erp.module.approval.domain.model;

import lombok.Data;
import xbb.ai.erp.module.approval.contract.ApprovalScene;

import java.util.List;

@Data
public class ApprovalFlowDefinition {
    private Long id;
    private String corpid;
    private String businessCode;
    private ApprovalScene approvalScene;
    private String flowCode;
    private String flowName;
    private Integer version;
    private Integer priority;
    private ApprovalFlowStatus status;
    private String scopeJson;
    private String creatorId;
    private String modifyId;
    private List<ApprovalFlowNode> nodes;
}
