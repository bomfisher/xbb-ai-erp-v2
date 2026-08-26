package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;

@Data
public class PaymentMainDTO {
    private Long id;
    private String corpid;
    private String paymentNo;
    private Long supplierId;
    private Long paymentDate;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal writtenOffAmount;
    private java.math.BigDecimal remainingAmount;
    private String paymentType;
    private String paymentMethod;
    private Long bankAccountId;
    private String bankTransactionNo;
    private Integer status;
    private String remark;
    private String creatorId;
    private String modifyId;
}
