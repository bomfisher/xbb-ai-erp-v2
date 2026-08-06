package xbb.ai.erp.module.product.admin.vo;

import lombok.Data;

@Data
public class ProductDraftDetailVO {

    private ProductDraftMetaVO draftMeta;
    private ProductDetailVO detail;

    @Data
    public static class ProductDraftMetaVO {
        private Long draftId;
        private String draftCode;
        private String draftName;
    }
}
