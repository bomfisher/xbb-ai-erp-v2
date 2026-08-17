package xbb.ai.erp.module.sales.admin.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SalesOrderItemDTO {
    private Long id;
    private Long warehouseId;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private String specification;
    private String unitName;
    private BigDecimal qty;
    private BigDecimal deliveredQty;
    private BigDecimal unitPrice;
    private BigDecimal taxRate;
    private BigDecimal amount;
}
