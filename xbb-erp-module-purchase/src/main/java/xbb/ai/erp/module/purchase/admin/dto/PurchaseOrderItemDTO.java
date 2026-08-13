package xbb.ai.erp.module.purchase.admin.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class PurchaseOrderItemDTO {
    private Long id;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private String specification;
    private Long warehouseId;
    private BigDecimal currentStock;
    private String unitName;
    private BigDecimal qty;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
}
