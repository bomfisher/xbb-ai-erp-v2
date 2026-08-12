package xbb.ai.erp.module.masterdata.application.port;

import java.util.List;
import xbb.ai.erp.module.masterdata.application.pojo.WarehouseSaveDraftPojo;

public interface WarehouseDraftRepository {
    String saveDraft(WarehouseSaveDraftPojo draft);
    List<WarehouseSaveDraftPojo> listDrafts(String corpid, int limit);
    WarehouseSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
