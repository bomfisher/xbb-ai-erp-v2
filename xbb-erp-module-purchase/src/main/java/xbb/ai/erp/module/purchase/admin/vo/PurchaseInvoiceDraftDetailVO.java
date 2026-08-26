package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSourceSelectionDTO;

@Data
public class PurchaseInvoiceDraftDetailVO {
    private String draftCode;
    private PurchaseInvoiceMainDTO main;
    private PurchaseInvoiceSourceSelectionDTO sourceSelection;
}
