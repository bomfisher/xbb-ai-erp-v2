package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseInboundListItemVO {
    private Long id;
    private String inboundNo;
    private Long purchaseOrderId;
    private Long supplierId;
    private String supplierName;
    private Long warehouseId;
    private String inboundDate;
    private java.math.BigDecimal totalAmount;
    private String status;
    private String remark;
    private String creatorId;
    private String modifyId;
}
