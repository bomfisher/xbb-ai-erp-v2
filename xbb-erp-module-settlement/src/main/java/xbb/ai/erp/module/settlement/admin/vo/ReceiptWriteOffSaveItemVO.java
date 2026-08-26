package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;

@Data
public class ReceiptWriteOffSaveItemVO {
    private Long id;
    private String writeoffNo;
    private Long customerId;
    private Long receiptId;
    private Long receivableId;
    private Long writeoffDate;
    private java.math.BigDecimal amount;
    private String remark;
}
