package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseRequestItemListItemVO {
    private Long id;
    private Long requestId;
    private Integer lineNo;
    private Long skuId;
    private String skuCodeSnapshot;
    private String skuNameSnapshot;
    private java.math.BigDecimal requestQty;
    private java.math.BigDecimal reservedQty;
    private java.math.BigDecimal executedQty;
    private java.math.BigDecimal closedQty;
    private Long addTime;
    private Long updateTime;
}
