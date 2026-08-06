package xbb.ai.erp.module.purchase.domain.model;

import lombok.Data;

@Data
public class PurchaseInboundItem {
    private Long id;
    private String corpid;
    private Long inboundId;
    private Integer lineNo;
    private Long sourceLineId;
    private Long skuId;
    private Long warehouseId;
    private String batchNo;
    private String serialNo;
    private Long produceDate;
    private Long expireDate;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal qualifiedQty;
    private java.math.BigDecimal unqualifiedQty;
    private java.math.BigDecimal grossPrice;
    private java.math.BigDecimal netPrice;
    private java.math.BigDecimal taxRate;
    private java.math.BigDecimal grossAmount;
    private java.math.BigDecimal netAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal inventoryCostAmount;
    private Integer version;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
