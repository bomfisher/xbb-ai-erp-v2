package xbb.ai.erp.module.sales.domain.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SalesInvoiceLineSource {
    private Long id;
    private String corpid;
    private Long salesInvoiceLineId;
    private String sourceType;
    private Long sourceId;
    private Long sourceLineId;
    private BigDecimal quantity;
    private BigDecimal untaxedAmount;
    private BigDecimal taxAmount;
    private BigDecimal amount;
    private String creatorId;
    private String modifyId;
}
