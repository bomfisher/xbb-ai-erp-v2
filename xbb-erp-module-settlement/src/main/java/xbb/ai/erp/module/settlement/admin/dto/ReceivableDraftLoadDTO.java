package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ReceivableDraftLoadDTO extends BaseDTO {
    private String draftCode;
}
