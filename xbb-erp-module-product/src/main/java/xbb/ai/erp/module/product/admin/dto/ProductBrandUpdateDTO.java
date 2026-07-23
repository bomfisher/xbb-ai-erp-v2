package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductBrandUpdateDTO extends BaseDTO {

    private Long id;
    private String brandCode;
    private String brandName;
    private Integer sortNo;
    private Integer enableStatus;
}
