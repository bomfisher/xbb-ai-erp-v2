package xbb.ai.erp.module.sales.domain.model;

import lombok.Data;

@Data
public class SalesOutbound {
    private Long id;
    private String corpid;
    private String outboundNo;
    private Long salesOrderId;
    private Long customerId;
    private String customerName;
    private Long warehouseId;
    private Long outboundDate;
    private java.math.BigDecimal totalAmount;
    private Integer status;
    private String remark;
    private Integer auditStatus;
    private String creatorId;
    private String modifyId;
}
