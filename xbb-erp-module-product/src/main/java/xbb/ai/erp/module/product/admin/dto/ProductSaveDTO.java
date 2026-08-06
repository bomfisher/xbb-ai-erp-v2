package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;
import xbb.ai.erp.base.common.dto.BaseDTO;

import java.util.List;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductSaveDTO extends BaseDTO {

    private ProductMainDTO main;
    private List<ProductSkuItemDTO> skus;
}
