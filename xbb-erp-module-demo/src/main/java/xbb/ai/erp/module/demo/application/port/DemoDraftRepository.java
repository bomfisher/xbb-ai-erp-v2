package xbb.ai.erp.module.demo.application.port;

import xbb.ai.erp.module.demo.application.pojo.DemoQueryPojo;

import java.util.List;

public interface DemoDraftRepository {
    String saveDraft(String corpid, String draftCode, String draftTitle, DemoQueryPojo payload);

    List<DemoQueryPojo> listDrafts(String corpid, int limit);

    DemoQueryPojo loadDraft(String corpid, String draftCode);

    void removeDraft(String corpid, String draftCode);
}
