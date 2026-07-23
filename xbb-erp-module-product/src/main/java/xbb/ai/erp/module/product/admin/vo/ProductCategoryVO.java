package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

@Data
public class ProductCategoryVO {

    private Long id;
    private String categoryCode;
    private String categoryName;
    private Long parentId;
    private Integer categoryLevel;
    private Integer sortNo;
    private Integer enableStatus;
}
