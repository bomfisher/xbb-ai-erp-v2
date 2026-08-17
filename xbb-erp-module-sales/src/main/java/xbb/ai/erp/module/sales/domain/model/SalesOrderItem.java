package xbb.ai.erp.module.sales.domain.model;

import lombok.Data;

@Data
public class SalesOrderItem {
    private Long id;
    private String corpid;
    private Long salesOrderId;
    private Long warehouseId;
    private Integer lineNo;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private String specification;
    private String unitName;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal deliveredQty;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal taxRate;
    private java.math.BigDecimal amount;
    private Integer outboundStatus;
    private String creatorId;
    private String modifyId;
}
