package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class PurchaseOrderListDTO extends BaseDTO {
    private Long id;
    private String corpid;
    private Long purchaseOrgId;
    private String orderNo;
    private Long vendorId;
    private String vendorNameSnapshot;
    private String purchaserId;
    private Long warehouseId;
    private Long settlementMethodId;
    private String currencyCode;
    private Long deliveryDate;
    private String sourceType;
    private String sourceNo;
    private Integer salesLinkedFlag;
    private String bizStatus;
    private String approvalStatus;
    private String executionStatus;
    private String receiptStatus;
    private String inboundStatus;
    private String payableStatus;
    private String invoiceStatus;
    private String paymentStatus;
    private Integer periodLockedFlag;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private String groupByStr;
    private String orderByStr;
}
