package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;

@Data
public class ProductSkuItemDTO {

    private Long skuId;
    private String skuCode;
    private String skuName;
    private String specSignature;
    private String specSnapshot;
    private String mnemonicCode;
    private String mainBarcode;
    private Integer canPurchase;
    private Integer canSale;
    private Integer canInventory;
    private Integer canProduce;
    private Integer skuEnableStatus;
    private Integer listingStatus;
}
