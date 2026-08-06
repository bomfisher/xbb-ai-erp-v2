package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseInboundListDTO extends BaseDTO {
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
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
}
