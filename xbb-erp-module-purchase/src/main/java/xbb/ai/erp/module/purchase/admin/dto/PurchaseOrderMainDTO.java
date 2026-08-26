package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;

@Data
public class PurchaseOrderMainDTO {
    private Long id;
    private String corpid;
    private String orderNo;
    private Long supplierId;
    private String supplierName;
    private Long orderDate;
    private Long expectedDate;
    private java.math.BigDecimal totalAmount;
    private Integer invoiceStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
}
