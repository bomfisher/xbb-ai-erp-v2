package xbb.ai.erp.module.purchase.admin.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PurchaseInvoiceLineDTO {
    private Long id;
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
    private String sourceType;
    private Long sourceId;
    private Long sourceLineId;
}
