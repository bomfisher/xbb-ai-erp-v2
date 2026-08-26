package xbb.ai.erp.module.inventory.domain.pojo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class StockBalanceQueryPojo {
    private Long id;
    private Long skuId;
    private Long warehouseId;
    private BigDecimal qty;
    private BigDecimal lockedQty;
    private BigDecimal availableQty;
    private BigDecimal totalCost;
    private BigDecimal unitCost;
}
