package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuSupplierRelationMainDTO {

    private Long id;
    private Long skuId;
    private Long supplierId;
    private BigDecimal purchasePrice;
    private Integer deliveryCycleDay;
    private BigDecimal minOrderQty;
    private String supplierSkuCode;
    private Integer defaultFlag;
    private Integer enableStatus;
    private String remark;
}
