package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class SalesOutboundSaveDTO extends BaseDTO {
    private SalesOutboundMainDTO main;
    private List<SalesOutboundItemDTO> items = List.of();
}
