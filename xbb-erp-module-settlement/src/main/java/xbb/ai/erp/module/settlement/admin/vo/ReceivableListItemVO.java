package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;

@Data
public class ReceivableListItemVO {
    private String id;
    private String receivableNo;
    private String customerId;
    private String sourceType;
    private String sourceInvoiceId;
    private String openingBatchId;
    private String receivableDate;
    private String dueDate;
    private String amount;
    private String writtenOffAmount;
    private String remainingAmount;
    private String status;
    private String auditStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
}
