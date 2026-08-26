package xbb.ai.erp.module.approval.infrastructure.persistence.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalNodeApproverPO extends BaseEntity {
    private Long flowNodeId;
    private String approverType;
    private String approverValue;
    private Integer superiorLevel;
}
