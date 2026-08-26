package xbb.ai.erp.module.settlement.application.port;

import java.util.List;
import xbb.ai.erp.module.settlement.application.pojo.PayableSaveDraftPojo;

public interface PayableDraftRepository {
    String saveDraft(PayableSaveDraftPojo draft);
    List<PayableSaveDraftPojo> listDrafts(String corpid, int limit);
    PayableSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
