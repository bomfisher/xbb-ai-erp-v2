package xbb.ai.erp.module.settlement.admin.vo;

import lombok.Data;

@Data
public class ReceiptWriteOffListItemVO {
    private Long id;
    private Long customerId;
    private String writeoffNo;
    private Long receiptId;
    private Long receivableId;
    private Long writeoffDate;
    private String amount;
    private String remark;
}
