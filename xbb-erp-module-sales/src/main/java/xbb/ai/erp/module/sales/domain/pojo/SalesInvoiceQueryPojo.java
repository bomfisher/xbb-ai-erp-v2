package xbb.ai.erp.module.sales.domain.pojo;

import lombok.Data;

@Data
public class SalesInvoiceQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String invoiceNo;
    private Long customerId;
    private Long invoiceDate;
    private Long dueDate;
    private String paymentTerm;
    private java.math.BigDecimal untaxedAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal amount;
    private String invoiceType;
    private String status;
    private Long auditTime;
}
