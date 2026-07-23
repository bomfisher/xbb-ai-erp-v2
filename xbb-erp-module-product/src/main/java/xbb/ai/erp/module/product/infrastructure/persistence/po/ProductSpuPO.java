package xbb.ai.erp.module.product.infrastructure.persistence.po;

import lombok.Data;

@Data
public class ProductSpuPO {

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
    private Integer del;
    private Long addTime;
    private Long updateTime;
    private String creatorId;
    private String modifyId;
}
