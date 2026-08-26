package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceSourceSelectionDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceLineDTO;
import java.util.List;

@Data
public class PurchaseInvoiceSaveItemVO {
    private PurchaseInvoiceMainDTO main;
    private PurchaseInvoiceSourceSelectionDTO sourceSelection;
    private List<PurchaseInvoiceLineDTO> lines;
}
