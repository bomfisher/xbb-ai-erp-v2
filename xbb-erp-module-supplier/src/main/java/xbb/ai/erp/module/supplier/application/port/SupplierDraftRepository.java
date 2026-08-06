package xbb.ai.erp.module.supplier.application.port;

import xbb.ai.erp.module.supplier.application.pojo.SupplierSaveDraftPojo;

import java.util.List;

public interface SupplierDraftRepository {
    String saveDraft(SupplierSaveDraftPojo draft);

    List<SupplierSaveDraftPojo> listDrafts(String corpid, int limit);

    SupplierSaveDraftPojo loadDraft(String corpid, String draftCode);

    void removeDraft(String corpid, String draftCode);
}
