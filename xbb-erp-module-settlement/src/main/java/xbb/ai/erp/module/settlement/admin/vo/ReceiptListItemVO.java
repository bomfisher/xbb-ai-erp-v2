package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;

@Data
public class ReceiptListItemVO {
    private String id;
    private String receiptNo;
    private String customerId;
    private String receiptDate;
    private String amount;
    private String writtenOffAmount;
    private String remainingAmount;
    private String receiptType;
    private String paymentMethod;
    private String bankAccountId;
    private String bankTransactionNo;
    private String status;
    private String remark;
    private String creatorId;
    private String modifyId;
}
