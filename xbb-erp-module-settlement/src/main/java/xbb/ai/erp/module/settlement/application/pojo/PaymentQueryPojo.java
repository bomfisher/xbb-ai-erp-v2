package xbb.ai.erp.module.settlement.application.pojo;

import lombok.Data;

@Data
public class PaymentQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
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
}
