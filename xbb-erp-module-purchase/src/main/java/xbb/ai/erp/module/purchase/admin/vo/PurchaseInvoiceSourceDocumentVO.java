package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseInvoiceSourceDocumentVO {
    private Long id;
    private String documentNo;
    private Long supplierId;
    private String sourceType;
}
