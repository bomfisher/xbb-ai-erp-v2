package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

@Data
public class ProductSpuSkuListVO {

    private Long spuId;
    private String spuCode;
    private String spuName;
    private Long skuId;
    private String skuCode;
    private String skuName;
    private String mainBarcode;
    private String productType;
}
