package xbb.ai.erp.module.inventory.domain.model;

import lombok.Data;

@Data
public class StockCostTransaction {
    private Long id;
    private String corpid;
    private Long warehouseId;
    private Long skuId;
    private String actionType;
    private String businessCode;
    private Long sourceId;
    private java.math.BigDecimal qtyBefore;
    private java.math.BigDecimal qtyChange;
    private java.math.BigDecimal qtyAfter;
    private java.math.BigDecimal totalCostBefore;
    private java.math.BigDecimal totalCostChange;
    private java.math.BigDecimal totalCostAfter;
    private java.math.BigDecimal unitCostBefore;
    private java.math.BigDecimal unitCost;
    private java.math.BigDecimal unitCostAfter;
    private java.math.BigDecimal tailDifference;
    private String reason;
    private String idempotencyKey;
    private String operatorId;
    private java.time.LocalDateTime occurredAt;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
