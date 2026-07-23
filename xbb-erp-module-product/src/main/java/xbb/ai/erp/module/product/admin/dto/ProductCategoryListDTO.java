package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductCategoryListDTO extends BaseDTO {

    private Integer offset;
    private Integer pageSize;
    private String categoryCode;
    private String categoryName;
}
