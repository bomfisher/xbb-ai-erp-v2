package xbb.ai.erp.module.approval.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.approval.domain.model.ApprovalApproverType;

@Data
public class ApprovalFlowApproverVO {
    private ApprovalApproverType approverType;
    private String approverValue;
    private Integer superiorLevel;
}
