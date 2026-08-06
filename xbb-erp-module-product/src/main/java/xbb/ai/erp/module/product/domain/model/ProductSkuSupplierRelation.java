package xbb.ai.erp.module.product.domain.model;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class ProductSkuSupplierRelation {

    private Long id;
    private String corpid;
    private Long skuId;
    private Long supplierId;
    private BigDecimal purchasePrice;
    private Integer deliveryCycleDay;
    private BigDecimal minOrderQty;
    private String supplierSkuCode;
    private Integer defaultFlag;
    private Integer enableStatus;
    private String remark;
    private Integer version;
    private Integer del;
    private String creatorId;
    private String modifyId;
    private Long addTime;
    private Long updateTime;
}
