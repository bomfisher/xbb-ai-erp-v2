package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class SettlementSelectionFillDTO extends BaseDTO {
    private String fieldAttr;
    private Long referenceId;
}
