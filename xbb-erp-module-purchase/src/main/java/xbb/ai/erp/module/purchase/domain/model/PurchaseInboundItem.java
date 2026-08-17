package xbb.ai.erp.module.purchase.domain.model;

import lombok.Data;

@Data
public class PurchaseInboundItem {
    private Long id;
    private String corpid;
    private Long purchaseInboundId;
    private Long purchaseOrderItemId;
    private Long skuId;
    private String skuName;
    private String unitName;
    private Long warehouseId;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal costUnit;
    private java.math.BigDecimal costAmount;
    private String creatorId;
    private String modifyId;
}
