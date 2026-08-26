package xbb.ai.erp.module.settlement.admin.dto;

import lombok.Data;

@Data
public class ReceivableMainDTO {
    private Long id;
    private String corpid;
    private String receivableNo;
    private Long customerId;
    private String sourceType;
    private Long sourceInvoiceId;
    private Long openingBatchId;
    private Long receivableDate;
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
