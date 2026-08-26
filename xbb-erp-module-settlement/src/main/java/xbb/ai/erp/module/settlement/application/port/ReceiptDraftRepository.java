package xbb.ai.erp.module.settlement.application.port;

import java.util.List;
import xbb.ai.erp.module.settlement.application.pojo.ReceiptSaveDraftPojo;

public interface ReceiptDraftRepository {
    String saveDraft(ReceiptSaveDraftPojo draft);
    List<ReceiptSaveDraftPojo> listDrafts(String corpid, int limit);
    ReceiptSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
