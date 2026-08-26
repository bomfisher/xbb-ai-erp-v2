package xbb.ai.erp.module.settlement.domain.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ReceiptWriteOff {
    private Long id;
    private String corpid;
    private Long customerId;
    private String writeoffNo;
    private Long receiptId;
    private Long receivableId;
    private Long writeoffDate;
    private BigDecimal amount;
    private Integer status;
    private Long reversedTime;
    private String remark;
    private String creatorId;
    private String modifyId;
}
