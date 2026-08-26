package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;

@Data
public class PurchaseInvoiceSourceSelectionDTO {
    private String sourceType;
    private Long sourceId;
    private String manualReason;
}
