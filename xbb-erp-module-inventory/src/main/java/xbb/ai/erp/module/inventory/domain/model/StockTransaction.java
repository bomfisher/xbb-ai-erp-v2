package xbb.ai.erp.module.inventory.domain.model;

import lombok.Data;

@Data
public class StockTransaction {
    private Long id;
    private String corpid;
    private Long warehouseId;
    private Long skuId;
    private String actionType;
    private java.math.BigDecimal qtyBefore;
    private java.math.BigDecimal qtyChange;
    private java.math.BigDecimal qtyAfter;
    private String sourceType;
    private Long sourceId;
    private String idempotencyKey;
    private String operatorId;
    private java.time.LocalDateTime occurredAt;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
