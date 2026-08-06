package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;

@Data
public class PurchaseInboundQueryPojo {
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
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
    private String inventoryFlowNo;
    private String payableTriggerStatus;
    private String payableNo;
    private String invoiceSourceStatus;
    private Integer periodLockedFlag;
}
