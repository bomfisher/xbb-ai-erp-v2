package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSourceSelectionDTO;

@Data
public class PurchaseInvoiceSaveDraftPojo {
    private String corpid;
    private String draftCode;
    private String draftTitle;
    private PurchaseInvoiceMainDTO main;
    private PurchaseInvoiceSourceSelectionDTO sourceSelection;
    private Long updatedTime;
}
