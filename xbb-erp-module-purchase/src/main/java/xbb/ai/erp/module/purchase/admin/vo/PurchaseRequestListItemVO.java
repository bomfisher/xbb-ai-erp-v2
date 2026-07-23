package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseRequestListItemVO {
    private Long id;
    private Long purchaseOrgId;
    private String requestNo;
    private String sourceType;
    private String bizStatus;
    private String approvalStatus;
    private java.math.BigDecimal grossAmount;
    private java.math.BigDecimal netAmount;
    private java.math.BigDecimal taxAmount;
    private Long addTime;
    private Long updateTime;
}
