package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesOutboundSelectionFillDTO extends BaseDTO {
    private String fieldAttr;
    private Long referenceId;
}
