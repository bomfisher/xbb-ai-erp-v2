package xbb.ai.erp.module.approval.infrastructure.persistence.po;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalInstanceActionLogPO extends BaseEntity {

    private String instanceId;

    private Integer nodeNo;

    private String operatorId;

    private String action;

    private String comment;

    private Long operatedAt;
}
