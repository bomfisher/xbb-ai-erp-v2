package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseInvoiceListItemVO {
    private String id;
    private String invoiceNo;
    private String supplierInvoiceNo;
    private String supplierId;
    private String invoiceDate;
    private String dueDate;
    private String paymentTerm;
    private String untaxedAmount;
    private String taxAmount;
    private String amount;
    private String invoiceType;
    private String status;
    private String auditStatus;
    private String remark;
}
