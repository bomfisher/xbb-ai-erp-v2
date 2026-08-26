package xbb.ai.erp.module.approval.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

/**
 * 审批中心列表查询条件。
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ApprovalInstanceListDTO extends BaseDTO {

    private String view;

    private String status;

    private String businessCode;

    private String keyword;
}
