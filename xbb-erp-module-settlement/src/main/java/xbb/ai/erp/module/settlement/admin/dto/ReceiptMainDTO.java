package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;

@Data
public class ReceiptMainDTO {
    private Long id;
    private String corpid;
    private String receiptNo;
    private Long customerId;
    private Long receiptDate;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal writtenOffAmount;
    private java.math.BigDecimal remainingAmount;
    private String receiptType;
    private String paymentMethod;
    private Long bankAccountId;
    private String bankTransactionNo;
    private Integer status;
    private String remark;
    private String creatorId;
    private String modifyId;
}
