package xbb.ai.erp.module.approval.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalFlowIdDTO extends BaseDTO {
    private Long id;
}
