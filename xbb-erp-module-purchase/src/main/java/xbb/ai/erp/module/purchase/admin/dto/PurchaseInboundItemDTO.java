package xbb.ai.erp.module.purchase.admin.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PurchaseInboundItemDTO {
    private Long id;
    private Long purchaseOrderItemId;
    private Long skuId;
    private String skuName;
    private String unitName;
    private BigDecimal qty;
    private BigDecimal unitPrice;
    private BigDecimal costUnit;
}
