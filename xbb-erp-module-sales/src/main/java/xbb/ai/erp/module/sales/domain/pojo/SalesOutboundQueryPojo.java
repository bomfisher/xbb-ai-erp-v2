package xbb.ai.erp.module.sales.domain.pojo;

import lombok.Data;

@Data
public class SalesOutboundQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String outboundNo;
    private Long salesOrderId;
    private Long customerId;
    private Long warehouseId;
    private Long outboundDate;
    private java.math.BigDecimal totalAmount;
    private String status;
    private Integer auditStatus;
}
