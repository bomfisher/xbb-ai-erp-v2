package xbb.ai.erp.module.settlement.admin.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentInfoDTO {
    private Long id;
    private Long bankAccountId;
    private String paymentMethod;
    private BigDecimal amount;
    private String transactionNo;
    private String remark;
}
