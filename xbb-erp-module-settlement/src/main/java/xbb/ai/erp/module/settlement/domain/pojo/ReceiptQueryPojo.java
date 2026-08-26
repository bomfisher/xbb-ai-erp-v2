package xbb.ai.erp.module.settlement.domain.pojo;

import lombok.Data;

@Data
public class ReceiptQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
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
    private String creatorId;
    private String modifyId;
}
