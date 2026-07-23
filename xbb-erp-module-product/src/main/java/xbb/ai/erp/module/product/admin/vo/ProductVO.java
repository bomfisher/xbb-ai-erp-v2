package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

@Data
public class ProductVO {

    private Long spuId;
    private Long skuId;
    private String spuCode;
    private String spuName;
    private Long categoryId;
    private Long brandId;
    private String productType;
    private Integer enableSpec;
    private String description;
    private String imageUrl;
    private Integer spuEnableStatus;
    private String skuCode;
    private String skuName;
    private String mnemonicCode;
    private String mainBarcode;
    private Integer canPurchase;
    private Integer canSale;
    private Integer canInventory;
    private Integer canProduce;
    private Integer skuEnableStatus;
    private Integer listingStatus;
}
