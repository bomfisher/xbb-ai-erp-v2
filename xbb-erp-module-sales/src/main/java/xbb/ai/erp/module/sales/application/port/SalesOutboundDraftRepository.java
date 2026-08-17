package xbb.ai.erp.module.sales.application.port;

import java.util.List;
import xbb.ai.erp.module.sales.application.pojo.SalesOutboundSaveDraftPojo;

public interface SalesOutboundDraftRepository {
    String saveDraft(SalesOutboundSaveDraftPojo draft);
    List<SalesOutboundSaveDraftPojo> listDrafts(String corpid, int limit);
    SalesOutboundSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
