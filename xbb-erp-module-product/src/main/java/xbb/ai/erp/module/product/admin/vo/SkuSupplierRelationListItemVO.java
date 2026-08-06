package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class SkuSupplierRelationListItemVO {

    private Long id;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private String specSnapshot;
    private Long supplierId;
    private String supplierCode;
    private String supplierName;
    private BigDecimal purchasePrice;
    private Integer deliveryCycleDay;
    private BigDecimal minOrderQty;
    private String supplierSkuCode;
    private Integer defaultFlag;
    private Integer enableStatus;
    private String remark;
    private Long updateTime;
}
