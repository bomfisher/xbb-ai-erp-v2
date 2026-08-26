package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;

@Data
public class SalesInvoiceMainDTO {
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
    private String invoiceType;
//    private String status;
    private Integer auditStatus;
    private Long auditTime;
    private Long postedTime;
    private String remark;
    private String creatorId;
    private String modifyId;
}
