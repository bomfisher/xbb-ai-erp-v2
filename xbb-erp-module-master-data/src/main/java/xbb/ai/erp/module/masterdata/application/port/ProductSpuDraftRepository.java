package xbb.ai.erp.module.masterdata.application.port;

import java.util.List;
import xbb.ai.erp.module.masterdata.application.pojo.ProductSpuSaveDraftPojo;

public interface ProductSpuDraftRepository {
    String saveDraft(ProductSpuSaveDraftPojo draft);
    List<ProductSpuSaveDraftPojo> listDrafts(String corpid, int limit);
    ProductSpuSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
