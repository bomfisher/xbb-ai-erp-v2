package xbb.ai.erp.module.purchase.domain.model;

import lombok.Data;

@Data
public class PurchaseRequestItem {
    private Long id;
    private String corpid;
    private Long requestId;
    private Integer lineNo;
    private Long skuId;
    private String skuCodeSnapshot;
    private String skuNameSnapshot;
    private String specSnapshot;
    private Long purchaseUnitId;
    private java.math.BigDecimal requestQty;
    private java.math.BigDecimal reservedQty;
    private java.math.BigDecimal executedQty;
    private java.math.BigDecimal closedQty;
    private Long suggestedVendorId;
    private Long suggestedDeliveryDate;
    private Integer version;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
