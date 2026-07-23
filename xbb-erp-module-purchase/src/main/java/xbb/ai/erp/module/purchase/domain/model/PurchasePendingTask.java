package xbb.ai.erp.module.purchase.domain.model;

import lombok.Data;

@Data
public class PurchasePendingTask {
    private Long id;
    private String corpid;
    private Long purchaseOrgId;
    private String taskNo;
    private String sourceType;
    private Long sourceDocId;
    private Long sourceLineId;
    private String sourceDocNo;
    private Long skuId;
    private String skuCodeSnapshot;
    private String skuNameSnapshot;
    private java.math.BigDecimal needQty;
    private java.math.BigDecimal occupiedQty;
    private java.math.BigDecimal generatedRequestQty;
    private java.math.BigDecimal generatedOrderQty;
    private java.math.BigDecimal closedQty;
    private Long suggestedVendorId;
    private Long suggestedDeliveryDate;
    private Integer priorityLevel;
    private String taskStatus;
    private Integer salesLinkedFlag;
    private Integer version;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
