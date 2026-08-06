package xbb.ai.erp.module.purchase.application.port;

import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestSaveDraftPojo;

import java.util.List;

public interface PurchaseRequestDraftRepository {
    String saveDraft(PurchaseRequestSaveDraftPojo draft);

    List<PurchaseRequestSaveDraftPojo> listDrafts(String corpid, int limit);

    PurchaseRequestSaveDraftPojo loadDraft(String corpid, String draftCode);

    void removeDraft(String corpid, String draftCode);
}
