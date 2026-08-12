package xbb.ai.erp.module.masterdata.application.port;

import java.util.List;
import xbb.ai.erp.module.masterdata.application.pojo.CustomerSaveDraftPojo;

public interface CustomerDraftRepository {
    String saveDraft(CustomerSaveDraftPojo draft);
    List<CustomerSaveDraftPojo> listDrafts(String corpid, int limit);
    CustomerSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
