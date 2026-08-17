package xbb.ai.erp.module.sales.application.port;

import java.util.List;
import xbb.ai.erp.module.sales.application.pojo.SalesOrderSaveDraftPojo;

public interface SalesOrderDraftRepository {
    String saveDraft(SalesOrderSaveDraftPojo draft);
    List<SalesOrderSaveDraftPojo> listDrafts(String corpid, int limit);
    SalesOrderSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
