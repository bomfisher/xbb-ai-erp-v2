package xbb.ai.erp.module.inventory.domain.model;

import lombok.Data;

@Data
public class StockBalance {
    private Long id;
    private String corpid;
    private Long warehouseId;
    private Long skuId;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal lockedQty;
    private java.math.BigDecimal availableQty;
    private java.math.BigDecimal totalCost;
    private java.math.BigDecimal unitCost;
    private Integer version;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
