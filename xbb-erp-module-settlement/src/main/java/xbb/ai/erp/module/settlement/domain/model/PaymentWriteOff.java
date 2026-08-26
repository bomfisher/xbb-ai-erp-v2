package xbb.ai.erp.module.settlement.domain.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PaymentWriteOff {
    private Long id;
    private String corpid;
    private Long supplierId;
    private String writeoffNo;
    private Long paymentId;
    private Long payableId;
    private Long writeoffDate;
    private BigDecimal amount;
    private Integer status;
    private Long reversedTime;
    private String remark;
    private String creatorId;
    private String modifyId;
}
