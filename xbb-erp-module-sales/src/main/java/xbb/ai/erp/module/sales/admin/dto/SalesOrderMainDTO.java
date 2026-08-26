package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;

@Data
public class SalesOrderMainDTO {
    private Long id;
    private String corpid;
    private String orderNo;
    private Long customerId;
    private Long warehouseId;
    private Long orderDate;
    private Long deliveryDate;
    private java.math.BigDecimal totalAmount;
    private Integer status;
    private String remark;
    private Integer auditStatus;
    private Integer outboundStatus;
    private Integer receiptStatus;
    private Integer invoiceStatus;
    private String creatorId;
    private String modifyId;
}
