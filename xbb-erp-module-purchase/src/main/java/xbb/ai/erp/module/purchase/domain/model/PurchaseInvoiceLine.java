package xbb.ai.erp.module.purchase.domain.model;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PurchaseInvoiceLine {
    private Long id;
    private String corpid;
    private Long purchaseInvoiceId;
    private Integer lineNo;
    private Long productId;
    private String productName;
    private String specification;
    private String unitName;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
    private BigDecimal untaxedAmount;
    private BigDecimal taxAmount;
    private BigDecimal amount;
    private String remark;
    private String creatorId;
    private String modifyId;
}
