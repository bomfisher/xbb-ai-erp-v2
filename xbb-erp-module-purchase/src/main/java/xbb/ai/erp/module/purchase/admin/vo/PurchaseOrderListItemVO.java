package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseOrderListItemVO {
    private Long id;
    private Long purchaseOrgId;
    private String orderNo;
    private Long vendorId;
    private String vendorNameSnapshot;
    private String purchaserNameSnapshot;
    private String warehouseNameSnapshot;
    private String currencyCode;
    private Long deliveryDate;
    private String bizStatus;
    private String approvalStatus;
    private String executionStatus;
    private String receiptStatus;
    private String inboundStatus;
    private java.math.BigDecimal grossAmount;
    private java.math.BigDecimal netAmount;
    private java.math.BigDecimal taxAmount;
    private java.math.BigDecimal inboundedQtySummary;
    private java.math.BigDecimal uninboundedQtySummary;
    private java.math.BigDecimal closedQtySummary;
    private Long addTime;
    private Long updateTime;
}
