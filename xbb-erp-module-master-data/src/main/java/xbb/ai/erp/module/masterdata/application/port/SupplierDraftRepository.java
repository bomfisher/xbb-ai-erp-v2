package xbb.ai.erp.module.masterdata.application.port;

import java.util.List;
import xbb.ai.erp.module.masterdata.application.pojo.SupplierSaveDraftPojo;

public interface SupplierDraftRepository {
    String saveDraft(SupplierSaveDraftPojo draft);
    List<SupplierSaveDraftPojo> listDrafts(String corpid, int limit);
    SupplierSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
