package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;
import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductSaveItemVO {

    private ProductMainDTO main = new ProductMainDTO();
    private List<ProductSkuItemDTO> skus = new ArrayList<>();
}
