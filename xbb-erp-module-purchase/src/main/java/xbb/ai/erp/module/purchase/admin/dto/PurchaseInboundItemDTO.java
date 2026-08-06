package xbb.ai.erp.module.purchase.admin.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PurchaseInboundItemDTO {
    private Long id;
    private Long sourceLineId;
    private Long skuId;
    private Long warehouseId;
    private String batchNo;
    private String serialNo;
    private Long produceDate;
    private Long expireDate;
    private BigDecimal qty;
    private BigDecimal qualifiedQty;
    private BigDecimal unqualifiedQty;
    private BigDecimal grossPrice;
    private BigDecimal netPrice;
    private BigDecimal taxRate;
    private BigDecimal grossAmount;
    private BigDecimal netAmount;
    private BigDecimal taxAmount;
}
