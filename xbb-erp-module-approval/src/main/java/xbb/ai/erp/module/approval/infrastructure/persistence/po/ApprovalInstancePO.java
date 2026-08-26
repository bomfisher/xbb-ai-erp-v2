package xbb.ai.erp.module.approval.infrastructure.persistence.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalInstancePO extends BaseEntity {
    private String instanceId;
    private String corpid;
    private Long flowDefinitionId;
    private String flowCode;
    private Integer flowVersion;
    private String businessCode;
    private String approvalScene;
    private String subjectId;
    private String requestId;
    private String submitterId;
    private String subjectSummary;
    private String subjectSnapshotJson;
    private String flowSnapshotJson;
    private String status;
    private Integer currentNodeNo;
    private Long submittedAt;
    private Long completedAt;
}
