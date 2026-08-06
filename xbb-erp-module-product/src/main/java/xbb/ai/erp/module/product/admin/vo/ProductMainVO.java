package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

@Data
public class ProductMainVO {

    private Long spuId;
    private String spuCode;
    private String spuName;
    private Long categoryId;
    private Long brandId;
    private String productType;
    private Integer enableSpec;
    private String description;
    private String imageUrl;
    private Integer spuEnableStatus;
}
