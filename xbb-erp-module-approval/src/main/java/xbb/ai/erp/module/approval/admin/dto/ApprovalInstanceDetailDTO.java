package xbb.ai.erp.module.approval.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

/**
 * 审批实例详情查询条件。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalInstanceDetailDTO extends BaseDTO {

    private String instanceId;
}
