package xbb.ai.erp.module.customer.application.port;

import xbb.ai.erp.module.customer.application.pojo.CustomerSaveDraftPojo;

import java.util.List;

public interface CustomerDraftRepository {
    String saveDraft(CustomerSaveDraftPojo draft);

    List<CustomerSaveDraftPojo> listDrafts(String corpid, int limit);

    CustomerSaveDraftPojo loadDraft(String corpid, String draftCode);

    void removeDraft(String corpid, String draftCode);
}
