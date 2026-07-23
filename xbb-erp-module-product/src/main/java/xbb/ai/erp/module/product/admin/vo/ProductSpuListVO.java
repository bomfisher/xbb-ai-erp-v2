package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

@Data
public class ProductSpuListVO {

    private Long spuId;
    private String spuCode;
    private String spuName;
    private String productType;
    private Integer enableStatus;
}
