package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

@Data
public class ProductSkuListVO {

    private Long skuId;
    private String skuCode;
    private String skuName;
    private String mainBarcode;
    private Integer enableStatus;
    private Integer listingStatus;
}
