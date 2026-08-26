package xbb.ai.erp.module.purchase.domain.model;

import lombok.Data;

@Data
public class PurchaseInvoice {
    private Long id;
    private String corpid;
    private String invoiceNo;
    private String supplierInvoiceNo;
    private Long supplierId;
    private Long invoiceDate;
    private Long dueDate;
    private String paymentTerm;
    private java.math.BigDecimal untaxedAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal payableOpenedAmount;
    private java.math.BigDecimal payableAvailableAmount;
    private String invoiceType;
    private String status;
    private Integer auditStatus;
    private Long auditTime;
    private Long postedTime;
    private Long originalInvoiceId;
    private String remark;
    private String sourceType;
    private Long sourceId;
    private String manualReason;
    private String creatorId;
    private String modifyId;
}
