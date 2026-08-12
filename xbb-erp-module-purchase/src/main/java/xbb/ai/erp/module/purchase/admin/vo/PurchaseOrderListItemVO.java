package xbb.ai.erp.module.purchase.admin.vo;

import lombok.Data;

@Data
public class PurchaseOrderListItemVO {
    private Long id;
    private String orderNo;
    private Long supplierId;
    private String supplierName;
    private Long orderDate;
    private Long expectedDate;
    private java.math.BigDecimal totalAmount;
    private String status;
    private String remark;
    private String creatorId;
    private String modifyId;
}
