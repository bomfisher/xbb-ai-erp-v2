package xbb.ai.erp.module.purchase.application.pojo;

import lombok.Data;

@Data
public class PurchaseOrderQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String orderNo;
    private Long supplierId;
    private Long orderDate;
    private Long expectedDate;
    private java.math.BigDecimal totalAmount;
    private String status;
    private String remark;
}
