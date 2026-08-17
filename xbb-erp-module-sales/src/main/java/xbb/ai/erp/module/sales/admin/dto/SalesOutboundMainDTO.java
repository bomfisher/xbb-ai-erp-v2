package xbb.ai.erp.module.sales.admin.dto;

import lombok.Data;

@Data
public class SalesOutboundMainDTO {
    private Long id;
    private String corpid;
    private String outboundNo;
    private Long salesOrderId;
    private Long customerId;
    private String customerName;
    private Long warehouseId;
    private Long outboundDate;
    private java.math.BigDecimal totalAmount;
    private String status;
    private String remark;
    private Integer auditStatus;
    private String creatorId;
    private String modifyId;
}
