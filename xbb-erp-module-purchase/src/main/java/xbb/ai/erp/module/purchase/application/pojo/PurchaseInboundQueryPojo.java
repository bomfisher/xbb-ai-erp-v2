package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;

@Data
public class PurchaseInboundQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String inboundNo;
    private Long purchaseOrderId;
    private Long supplierId;
    private Long warehouseId;
    private Long inboundDate;
    private java.math.BigDecimal totalAmount;
    private String status;
    private String remark;
}
