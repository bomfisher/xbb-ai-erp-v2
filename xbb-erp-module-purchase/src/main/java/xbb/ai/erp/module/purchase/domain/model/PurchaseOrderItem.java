package xbb.ai.erp.module.purchase.domain.model;

import lombok.Data;

@Data
public class PurchaseOrderItem {
    private Long id;
    private String corpid;
    private Long orderId;
    private Integer lineNo;
    private Long skuId;
    private String skuCodeSnapshot;
    private String skuNameSnapshot;
    private String specSnapshot;
    private Long purchaseUnitId;
    private Long warehouseId;
    private java.math.BigDecimal orderQty;
    private java.math.BigDecimal receivedQty;
    private java.math.BigDecimal inboundedQty;
    private java.math.BigDecimal closedQty;
    private java.math.BigDecimal returnedQty;
    private java.math.BigDecimal grossPrice;
    private java.math.BigDecimal netPrice;
    private java.math.BigDecimal taxRate;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal grossAmount;
    private java.math.BigDecimal netAmount;
    private java.math.BigDecimal payableAmount;
    private java.math.BigDecimal paidAmount;
    private java.math.BigDecimal invoicedAmount;
    private Integer isGift;
    private String deliveryPlanSnapshot;
    private Integer version;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
