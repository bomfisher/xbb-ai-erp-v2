package xbb.ai.erp.module.product.domain.model;

import lombok.Data;

@Data
public class ProductSpu {

    private Long id;
    private String corpid;
    private String spuCode;
    private String spuName;
    private Long categoryId;
    private Long brandId;
    private String productType;
    private Integer enableSpec;
    private String description;
    private String imageUrl;
    private Integer enableStatus;
}
