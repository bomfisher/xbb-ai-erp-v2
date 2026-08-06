package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseInboundListItemVO {
    private Long id;
    private Long purchaseOrgId;
    private String inboundNo;
    private String sourceDocType;
    private Long vendorId;
    private Long warehouseId;
    private Long actualInboundTime;
    private String bizStatus;
    private String executionStatus;
    private java.math.BigDecimal grossAmount;
    private java.math.BigDecimal netAmount;
    private String payableTriggerStatus;
}
