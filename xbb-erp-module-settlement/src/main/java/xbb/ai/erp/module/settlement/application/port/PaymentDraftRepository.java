package xbb.ai.erp.module.settlement.application.port;

import java.util.List;
import xbb.ai.erp.module.settlement.application.pojo.PaymentSaveDraftPojo;

public interface PaymentDraftRepository {
    String saveDraft(PaymentSaveDraftPojo draft);
    List<PaymentSaveDraftPojo> listDrafts(String corpid, int limit);
    PaymentSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
