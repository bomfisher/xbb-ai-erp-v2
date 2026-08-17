package xbb.ai.erp.module.sales.domain.pojo;

import lombok.Data;

@Data
public class SalesOrderQueryPojo {
    private String corpid;
    private String keyword;
    private Integer pageNum;
    private Integer pageSize;
    private Integer offset;
    private Long id;
    private String orderNo;
    private Long customerId;
    private Long orderDate;
    private Long deliveryDate;
    private java.math.BigDecimal totalAmount;
    private Integer status;
    private Integer auditStatus;
    private Integer outboundStatus;
    private Integer receiptStatus;
}
