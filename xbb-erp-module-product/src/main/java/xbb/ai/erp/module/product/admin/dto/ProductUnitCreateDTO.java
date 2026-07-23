package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductUnitCreateDTO extends BaseDTO {

    private String unitCode;
    private String unitName;
    private Integer precisionNum;
    private Integer enableStatus;
}
