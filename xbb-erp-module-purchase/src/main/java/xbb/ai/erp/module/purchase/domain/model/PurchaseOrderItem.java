package xbb.ai.erp.module.purchase.domain.model;

import lombok.Data;

@Data
public class PurchaseOrderItem {
    private Long id;
    private String corpid;
    private Long purchaseOrderId;
    private Integer lineNo;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private String specification;
    private String unitName;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal inboundQty;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal taxRate;
    private java.math.BigDecimal amount;
    private Integer inboundStatus;
    private String creatorId;
    private String modifyId;
}
