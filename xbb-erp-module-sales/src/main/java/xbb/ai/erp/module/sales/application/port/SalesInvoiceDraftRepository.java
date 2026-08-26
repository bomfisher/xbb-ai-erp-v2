package xbb.ai.erp.module.sales.application.port;

import java.util.List;
import xbb.ai.erp.module.sales.application.pojo.SalesInvoiceSaveDraftPojo;

public interface SalesInvoiceDraftRepository {
    String saveDraft(SalesInvoiceSaveDraftPojo draft);
    List<SalesInvoiceSaveDraftPojo> listDrafts(String corpid, int limit);
    SalesInvoiceSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
