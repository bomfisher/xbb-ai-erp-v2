package xbb.ai.erp.module.inventory.admin.vo;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class StockQueryItemVO { private Long id; private Long skuId; private String skuCode; private String skuName; private String categoryName; private String specification; private Long warehouseId; private String warehouseCode; private String warehouseName; private BigDecimal qty; private BigDecimal lockedQty; private BigDecimal availableQty; private BigDecimal totalCost; private BigDecimal unitCost; }
