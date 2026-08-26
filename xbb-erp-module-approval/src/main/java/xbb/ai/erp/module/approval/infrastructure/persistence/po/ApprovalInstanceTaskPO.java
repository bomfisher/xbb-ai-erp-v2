package xbb.ai.erp.module.approval.infrastructure.persistence.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalInstanceTaskPO extends BaseEntity {
    private String instanceId;
    private Integer nodeNo;
    private String assigneeId;
    private String status;
    private String comment;
    private Long handledAt;
}
