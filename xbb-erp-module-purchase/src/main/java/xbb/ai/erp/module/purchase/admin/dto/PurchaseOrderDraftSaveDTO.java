package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderDraftSaveDTO extends PurchaseOrderSaveDTO {
    private PurchaseOrderDraftMetaDTO draftMeta = new PurchaseOrderDraftMetaDTO();
}
