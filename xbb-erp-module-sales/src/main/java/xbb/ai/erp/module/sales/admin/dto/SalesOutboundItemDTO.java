package xbb.ai.erp.module.sales.admin.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class SalesOutboundItemDTO {
    private Long id;
    private Long salesOrderItemId;
    private Long skuId;
    private String skuName;
    private String unitName;
    private BigDecimal qty;
    private BigDecimal unitPrice;
}
