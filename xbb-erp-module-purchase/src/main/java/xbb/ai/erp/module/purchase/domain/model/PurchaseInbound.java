package xbb.ai.erp.module.purchase.domain.model;

import lombok.Data;

@Data
public class PurchaseInbound {
    private Long id;
    private String corpid;
    private Long purchaseOrgId;
    private String inboundNo;
    private String sourceDocType;
    private Long sourceDocId;
    private Long vendorId;
    private Long warehouseId;
    private Long actualInboundTime;
    private String bizStatus;
    private String approvalStatus;
    private String executionStatus;
    private java.math.BigDecimal grossAmount;
    private java.math.BigDecimal netAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal inventoryCostAmount;
    private String inventoryFlowNo;
    private String payableTriggerStatus;
    private String payableNo;
    private String invoiceSourceStatus;
    private Integer periodLockedFlag;
    private Integer version;
    private String remark;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
