package xbb.ai.erp.module.product.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductSaveContextPojo {

    private String corpid;
    private String userId;
    private ProductMainDTO main;
    private List<ProductSkuItemDTO> skus = new ArrayList<>();
    private ProductSaveDraftPojo draft = new ProductSaveDraftPojo();
}
