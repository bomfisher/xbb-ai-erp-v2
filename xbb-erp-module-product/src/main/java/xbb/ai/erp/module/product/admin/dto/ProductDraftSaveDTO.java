package xbb.ai.erp.module.product.admin.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class ProductDraftSaveDTO extends ProductSaveDTO {

    private ProductDraftMetaDTO draftMeta;
}
