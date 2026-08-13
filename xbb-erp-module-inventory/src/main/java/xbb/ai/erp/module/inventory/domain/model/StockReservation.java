package xbb.ai.erp.module.inventory.domain.model;

import lombok.Data;

@Data
public class StockReservation {
    private Long id;
    private String corpid;
    private Long warehouseId;
    private Long skuId;
    private String sourceType;
    private Long sourceId;
    private Long sourceLineId;
    private java.math.BigDecimal reservedQty;
    private java.math.BigDecimal outboundQty;
    private java.math.BigDecimal releasedQty;
    private java.math.BigDecimal remainingQty;
    private String status;
    private java.time.LocalDateTime reservedAt;
    private java.time.LocalDateTime releasedAt;
    private String idempotencyKey;
    private Integer version;
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
