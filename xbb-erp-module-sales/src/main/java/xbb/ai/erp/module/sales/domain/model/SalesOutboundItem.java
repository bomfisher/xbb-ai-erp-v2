package xbb.ai.erp.module.sales.domain.model;

import lombok.Data;

@Data
public class SalesOutboundItem {
    private Long id;
    private String corpid;
    private Long salesOutboundId;
    private Long salesOrderItemId;
    private Long skuId;
    private String skuName;
    private Long warehouseId;
    private String unitName;
    private java.math.BigDecimal qty;
    private java.math.BigDecimal unitPrice;
    private java.math.BigDecimal amount;
    private java.math.BigDecimal costUnit;
    private java.math.BigDecimal costAmount;
    private Integer outboundStatus;
    private String creatorId;
    private String modifyId;
}
