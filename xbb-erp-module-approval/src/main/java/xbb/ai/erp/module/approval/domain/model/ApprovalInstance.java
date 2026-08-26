package xbb.ai.erp.module.approval.domain.model;

import lombok.Data;
import xbb.ai.erp.module.approval.contract.ApprovalScene;
import xbb.ai.erp.module.approval.contract.ApprovalStatus;

@Data
public class ApprovalInstance {
    private String instanceId;
    private String corpid;
    private Long flowDefinitionId;
    private String flowCode;
    private Integer flowVersion;
    private String businessCode;
    private ApprovalScene approvalScene;
    private String subjectId;
    private String requestId;
    private String submitterId;
    private String subjectSummary;
    private String subjectSnapshotJson;
    private String flowSnapshotJson;
    private ApprovalStatus status;
    private Integer currentNodeNo;
    private Long submittedAt;
    private Long completedAt;
}
