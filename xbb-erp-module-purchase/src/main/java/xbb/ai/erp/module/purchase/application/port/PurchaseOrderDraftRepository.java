package xbb.ai.erp.module.purchase.application.port;

import java.util.List;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveDraftPojo;

public interface PurchaseOrderDraftRepository {
    String saveDraft(PurchaseOrderSaveDraftPojo draft);
    List<PurchaseOrderSaveDraftPojo> listDrafts(String corpid, int limit);
    PurchaseOrderSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
