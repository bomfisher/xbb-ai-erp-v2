package xbb.ai.erp.module.purchase.domain.model;

import lombok.Data;

@Data
public class PurchaseOrder {
    private Long id;
    private String corpid;
    private Long purchaseOrgId;
    private String orderNo;
    private Long vendorId;
    private String vendorNameSnapshot;
    private String purchaserId;
    private String purchaserNameSnapshot;
    private Long warehouseId;
    private String warehouseNameSnapshot;
    private Long settlementMethodId;
    private String settlementMethodSnapshot;
    private String paymentTermSnapshot;
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
    private java.math.BigDecimal grossAmount;
    private java.math.BigDecimal netAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal inboundedQtySummary;
    private java.math.BigDecimal uninboundedQtySummary;
    private java.math.BigDecimal closedQtySummary;
    private java.math.BigDecimal payableAmountSummary;
    private java.math.BigDecimal paidAmountSummary;
    private java.math.BigDecimal invoicedAmountSummary;
    private Long lastInboundTime;
    private Long lastPayableTime;
    private Integer periodLockedFlag;
    private Integer version;
    private String remark;
    private Integer deleted;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
