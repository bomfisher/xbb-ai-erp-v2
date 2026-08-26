package xbb.ai.erp.module.purchase.admin.vo;

import java.util.List;
import lombok.Data;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInvoiceLineDTO;

@Data
public class PurchaseInvoiceSourcePreviewVO {
    private Long supplierId;
    private String sourceType;
    private Long sourceId;
    private List<PurchaseInvoiceLineDTO> lines;
}
