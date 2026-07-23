package xbb.ai.erp.module.product.domain.model;

import lombok.Data;

@Data
public class ProductBrand {

    private Long id;
    private String corpid;
    private String brandCode;
    private String brandName;
    private Integer sortNo;
    private Integer enableStatus;
}
