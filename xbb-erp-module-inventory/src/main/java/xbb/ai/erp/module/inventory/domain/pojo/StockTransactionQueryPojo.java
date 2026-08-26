package xbb.ai.erp.module.inventory.domain.pojo;

import lombok.Data;

@Data
public class StockTransactionQueryPojo {
    private Long id;
    private Long warehouseId;
    private Long skuId;
    private String sourceType;
    private Long sourceId;
    private java.time.LocalDateTime occurredAt;
    private java.math.BigDecimal qtyChange;
    private java.math.BigDecimal qtyAfter;
    private java.math.BigDecimal inboundUnitCost;
    private java.math.BigDecimal outboundUnitCost;
    private java.math.BigDecimal inboundCost;
    private java.math.BigDecimal outboundCost;
    private java.math.BigDecimal unitCostAfter;
    private java.math.BigDecimal totalCostAfter;
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
}
