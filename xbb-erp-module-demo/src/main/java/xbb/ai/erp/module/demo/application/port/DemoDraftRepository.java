package xbb.ai.erp.module.demo.application.port;

import java.util.List;
import xbb.ai.erp.module.demo.application.pojo.DemoSaveDraftPojo;

public interface DemoDraftRepository {
    String saveDraft(DemoSaveDraftPojo draft);
    List<DemoSaveDraftPojo> listDrafts(String corpid, int limit);
    DemoSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
