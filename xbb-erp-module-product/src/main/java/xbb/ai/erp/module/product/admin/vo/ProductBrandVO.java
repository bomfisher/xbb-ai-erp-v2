package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

@Data
public class ProductBrandVO {

    private Long id;
    private String brandCode;
    private String brandName;
    private Integer sortNo;
    private Integer enableStatus;
}
