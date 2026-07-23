package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductCategoryUpdateDTO extends BaseDTO {

    private Long id;
    private String categoryCode;
    private String categoryName;
    private Long parentId;
    private Integer categoryLevel;
    private Integer sortNo;
    private Integer enableStatus;
}
