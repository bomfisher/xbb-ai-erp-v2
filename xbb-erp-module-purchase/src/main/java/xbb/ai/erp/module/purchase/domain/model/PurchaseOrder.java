package xbb.ai.erp.module.purchase.domain.model;

import lombok.Data;

@Data
public class PurchaseOrder {
    private Long id;
    private String corpid;
    private String orderNo;
    private Long supplierId;
    private String supplierName;
    private Long orderDate;
    private Long expectedDate;
    private java.math.BigDecimal totalAmount;
    private Integer status;
    private Integer auditStatus;
    private Integer inboundStatus;
    private Integer paymentStatus;
    private Integer invoiceStatus;
    private String remark;
    private String creatorId;
    private String modifyId;
    private Long addTime;
    private Long updateTime;
    private Integer del;
}
