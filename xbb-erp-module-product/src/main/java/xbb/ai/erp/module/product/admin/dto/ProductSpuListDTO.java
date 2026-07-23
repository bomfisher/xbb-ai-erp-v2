package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSpuListDTO extends BaseDTO {

    private Integer offset;
    private Integer pageSize;
    private String spuCode;
    private String spuName;
}
