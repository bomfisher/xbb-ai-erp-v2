package xbb.ai.erp.module.purchase.domain.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PurchaseInvoiceLineSource {
    private Long id;
    private String corpid;
    private Long purchaseInvoiceLineId;
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
