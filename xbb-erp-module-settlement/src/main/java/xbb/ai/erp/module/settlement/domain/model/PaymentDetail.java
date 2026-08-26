package xbb.ai.erp.module.settlement.domain.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentDetail {

    private Long id;
    private String corpid;
    private Long paymentId;
    private Long bankAccountId;
    private String paymentMethod;
    private BigDecimal amount;
    private BigDecimal handlingFee;
    private String transactionNo;
    private String remark;
    private Integer sortNo;
    private String creatorId;
    private String modifyId;
}
