package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchasePendingTaskListItemVO {
    private Long id;
    private Long purchaseOrgId;
    private String taskNo;
    private String sourceType;
    private Long skuId;
    private String skuCodeSnapshot;
    private String skuNameSnapshot;
    private java.math.BigDecimal needQty;
    private java.math.BigDecimal occupiedQty;
    private java.math.BigDecimal generatedRequestQty;
    private java.math.BigDecimal generatedOrderQty;
    private java.math.BigDecimal closedQty;
    private Integer priorityLevel;
    private String taskStatus;
    private Integer salesLinkedFlag;
    private Long addTime;
    private Long updateTime;
}
