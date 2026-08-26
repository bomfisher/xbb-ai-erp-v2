package xbb.ai.erp.module.settlement.application.pojo;

import lombok.Data;

@Data
public class ReceivableQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
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
    private String creatorId;
    private String modifyId;
}
