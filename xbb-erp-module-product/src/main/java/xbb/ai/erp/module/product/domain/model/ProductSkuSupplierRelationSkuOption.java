package xbb.ai.erp.module.product.domain.model;

import lombok.Data;

@Data
public class ProductSkuSupplierRelationSkuOption {

    private Long skuId;
    private Long spuId;
    private String skuCode;
    private String skuName;
    private String specSnapshot;
}
