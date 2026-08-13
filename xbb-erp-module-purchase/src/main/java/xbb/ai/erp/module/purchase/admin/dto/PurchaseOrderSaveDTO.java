package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import java.util.List;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderSaveDTO extends BaseDTO {
    private PurchaseOrderMainDTO main;
    private List<PurchaseOrderItemDTO> items;
}
