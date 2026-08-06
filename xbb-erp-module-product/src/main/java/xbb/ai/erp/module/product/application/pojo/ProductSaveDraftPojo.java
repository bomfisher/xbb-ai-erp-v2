package xbb.ai.erp.module.product.application.pojo;

import lombok.Data;
import xbb.ai.erp.module.product.admin.dto.ProductMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSkuItemDTO;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductSaveDraftPojo {

    private String corpid;
    private Long draftId;
    private String draftCode;
    private String draftName;
    private Long updatedTime;
    private ProductMainDTO main = new ProductMainDTO();
    private List<ProductSkuItemDTO> skus = new ArrayList<>();
}
