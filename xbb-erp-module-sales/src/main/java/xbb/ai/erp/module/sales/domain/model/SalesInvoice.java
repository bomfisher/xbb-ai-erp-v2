package xbb.ai.erp.module.sales.domain.model;

import lombok.Data;

@Data
public class SalesInvoice {
    private Long id;
    private String corpid;
    private String invoiceNo;
    private Long customerId;
    private Long invoiceDate;
    private Long dueDate;
    private String paymentTerm;
    private java.math.BigDecimal untaxedAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal receivableOpenedAmount;
    private java.math.BigDecimal receivableAvailableAmount;
    private String invoiceType;
    private Long originalInvoiceId;
    private String status;
    private Integer auditStatus;
    private Long auditTime;
    private Long postedTime;
    private String remark;
    private String creatorId;
    private String modifyId;
}
