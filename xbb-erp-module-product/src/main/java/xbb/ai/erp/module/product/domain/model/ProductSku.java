package xbb.ai.erp.module.product.domain.model;

import lombok.Data;

@Data
public class ProductSku {

    private Long id;
    private String corpid;
    private Long spuId;
    private String skuCode;
    private String skuName;
    private String mnemonicCode;
    private String mainBarcode;
    private String specSignature;
    private String specSnapshot;
    private Integer canPurchase;
    private Integer canSale;
    private Integer canInventory;
    private Integer canProduce;
    private Integer enableStatus;
    private Integer listingStatus;
}
