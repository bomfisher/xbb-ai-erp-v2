package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;

@Data
public class ProductCategoryMainDTO {

    private Long id;
    private String corpid;
    private String categoryCode;
    private String categoryName;
    private Long parentId;
    private Integer categoryLevel;
    private Integer sortNo;
    private Integer enableStatus;
}
