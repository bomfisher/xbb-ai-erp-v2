package xbb.ai.erp.module.settlement.admin.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentWriteOffAllocationDTO {
    private Long paymentId;
    private Long payableId;
    private BigDecimal amount;
}
