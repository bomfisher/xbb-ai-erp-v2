package xbb.ai.erp.module.approval.infrastructure.persistence.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalFlowNodePO extends BaseEntity {
    private Long flowDefinitionId;
    private Integer nodeNo;
    private String nodeName;
    private String approvalMode;
}
