package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseOrderItemListItemVO {
    private Long id;
    private Long orderId;
    private Integer lineNo;
    private Long skuId;
    private String skuCodeSnapshot;
    private String skuNameSnapshot;
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
    private Long addTime;
    private Long updateTime;
}
