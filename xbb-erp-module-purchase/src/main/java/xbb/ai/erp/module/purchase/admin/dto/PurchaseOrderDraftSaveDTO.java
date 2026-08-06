package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.util.ArrayList;
import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderDraftSaveDTO extends BaseDTO {
    private PurchaseOrderMainDTO main = new PurchaseOrderMainDTO();
    private List<PurchaseOrderItemMainDTO> items = new ArrayList<>();
    private PurchaseOrderSectionStateDTO sectionState = new PurchaseOrderSectionStateDTO();
    private PurchaseOrderDraftMetaDTO draftMeta = new PurchaseOrderDraftMetaDTO();
}
