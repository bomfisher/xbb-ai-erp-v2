package xbb.ai.erp.module.approval.infrastructure.persistence.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalFlowDefinitionPO extends BaseEntity {
    private String corpid;
    private String businessCode;
    private String approvalScene;
    private String flowCode;
    private String flowName;
    private Integer version;
    private Integer priority;
    private String status;
    private String scopeJson;
    private String creatorId;
    private String modifyId;
}
