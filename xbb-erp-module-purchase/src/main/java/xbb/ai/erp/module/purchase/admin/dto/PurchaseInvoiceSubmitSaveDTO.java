package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseInvoiceSubmitSaveDTO extends PurchaseInvoiceSaveDTO {
    private PurchaseInvoiceDraftMetaDTO draftMeta = new PurchaseInvoiceDraftMetaDTO();
}
