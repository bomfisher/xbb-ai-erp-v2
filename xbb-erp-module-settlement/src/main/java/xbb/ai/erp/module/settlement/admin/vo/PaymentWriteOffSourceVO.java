package xbb.ai.erp.module.settlement.admin.vo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentWriteOffSourceVO {
    private Long id;
    private String code;
    private BigDecimal amount;
    private BigDecimal writtenOffAmount;
    private BigDecimal remainingAmount;
}
