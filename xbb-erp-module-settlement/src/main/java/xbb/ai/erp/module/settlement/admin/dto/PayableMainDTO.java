package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;

@Data
public class PayableMainDTO {
    private Long id;
    private String corpid;
    private String payableNo;
    private Long supplierId;
    private String sourceType;
    private Long sourceInvoiceId;
    private Long payableDate;
    private Long dueDate;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal writtenOffAmount;
    private java.math.BigDecimal remainingAmount;
    private Integer status;
    private Integer auditStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
}
