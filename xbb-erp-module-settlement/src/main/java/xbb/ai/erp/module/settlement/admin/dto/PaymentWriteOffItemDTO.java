package xbb.ai.erp.module.settlement.admin.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentWriteOffItemDTO {
    private Long payableId;
    private BigDecimal amount;
}
