package xbb.ai.erp.module.approval.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.approval.contract.ApprovalScene;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalFlowListDTO extends BaseDTO {
    private String businessCode;
    private ApprovalScene approvalScene;
}
