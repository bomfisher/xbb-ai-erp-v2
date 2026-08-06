package xbb.ai.erp.module.product.application.port;

import xbb.ai.erp.module.product.application.pojo.ProductSaveDraftPojo;

import java.util.List;

public interface ProductDraftRepository {

    String saveDraft(ProductSaveDraftPojo draft);

    List<ProductSaveDraftPojo> listDrafts(String corpid, int limit);

    ProductSaveDraftPojo loadDraft(String corpid, Long draftId);

    void removeDraft(String corpid, String draftCode);
}
