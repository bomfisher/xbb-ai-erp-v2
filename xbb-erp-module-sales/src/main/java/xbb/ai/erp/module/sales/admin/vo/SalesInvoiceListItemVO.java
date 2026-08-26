package xbb.ai.erp.module.sales.admin.vo;

import lombok.Data;

@Data
public class SalesInvoiceListItemVO {
    private String id;
    private String invoiceNo;
    private String customerId;
    private String invoiceDate;
    private String dueDate;
    private String paymentTerm;
    private String untaxedAmount;
    private String taxAmount;
    private String amount;
    private String invoiceType;
    private String status;
    private String auditStatus;
    private String auditTime;
    private String postedTime;
    private String remark;
    private String creatorId;
    private String modifyId;
}
