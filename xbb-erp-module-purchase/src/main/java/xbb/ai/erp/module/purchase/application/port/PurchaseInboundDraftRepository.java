package xbb.ai.erp.module.purchase.application.port;

import java.util.List;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseInboundSaveDraftPojo;

public interface PurchaseInboundDraftRepository {
    String saveDraft(PurchaseInboundSaveDraftPojo draft);
    List<PurchaseInboundSaveDraftPojo> listDrafts(String corpid, int limit);
    PurchaseInboundSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
