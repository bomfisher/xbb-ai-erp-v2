package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductCreateDTO extends BaseDTO {

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
