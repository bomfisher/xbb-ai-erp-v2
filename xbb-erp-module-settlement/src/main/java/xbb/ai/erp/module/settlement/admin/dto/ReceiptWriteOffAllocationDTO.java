package xbb.ai.erp.module.settlement.admin.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class ReceiptWriteOffAllocationDTO {
    private Long receiptId;
    private Long receivableId;
    private BigDecimal amount;
}
