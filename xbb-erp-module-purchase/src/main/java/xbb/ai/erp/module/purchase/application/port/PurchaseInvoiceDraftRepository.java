package xbb.ai.erp.module.purchase.application.port;

import java.util.List;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseInvoiceSaveDraftPojo;

public interface PurchaseInvoiceDraftRepository {
    String saveDraft(PurchaseInvoiceSaveDraftPojo draft);
    List<PurchaseInvoiceSaveDraftPojo> listDrafts(String corpid, int limit);
    PurchaseInvoiceSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
