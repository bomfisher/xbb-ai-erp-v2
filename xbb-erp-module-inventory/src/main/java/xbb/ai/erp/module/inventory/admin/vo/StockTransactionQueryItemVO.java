package xbb.ai.erp.module.inventory.admin.vo;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.Data;

@Data
public class StockTransactionQueryItemVO {
    private Long id;
    private String skuCode;
    private String skuName;
    private String specification;
    private String unitName;
    private String sourceType;
    private Long sourceId;
    private String sourceDocumentNo;
    private LocalDateTime occurredAt;
    private String warehouseName;
    private BigDecimal inboundQty;
    private BigDecimal inboundUnitCost;
    private BigDecimal inboundCost;
    private BigDecimal outboundQty;
    private BigDecimal outboundUnitCost;
    private BigDecimal outboundCost;
    private BigDecimal qtyAfter;
    private BigDecimal unitCostAfter;
    private BigDecimal totalCostAfter;
}
