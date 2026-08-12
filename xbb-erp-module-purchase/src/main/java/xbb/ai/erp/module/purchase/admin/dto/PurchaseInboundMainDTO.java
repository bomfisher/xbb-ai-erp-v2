package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;

@Data
public class PurchaseInboundMainDTO {
    private Long id;
    private String corpid;
    private String inboundNo;
    private Long purchaseOrderId;
    private Long supplierId;
    private String supplierName;
    private Long warehouseId;
    private Long inboundDate;
    private java.math.BigDecimal totalAmount;
    private String status;
    private String remark;
    private String creatorId;
    private String modifyId;
}
