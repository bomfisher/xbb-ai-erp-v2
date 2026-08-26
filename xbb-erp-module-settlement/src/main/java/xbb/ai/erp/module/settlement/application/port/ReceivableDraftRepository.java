package xbb.ai.erp.module.settlement.application.port;

import java.util.List;
import xbb.ai.erp.module.settlement.application.pojo.ReceivableSaveDraftPojo;

public interface ReceivableDraftRepository {
    String saveDraft(ReceivableSaveDraftPojo draft);
    List<ReceivableSaveDraftPojo> listDrafts(String corpid, int limit);
    ReceivableSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
