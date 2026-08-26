package xbb.ai.erp.module.masterdata.application.port;

import java.util.List;
import xbb.ai.erp.module.masterdata.application.pojo.FundAccountSaveDraftPojo;

public interface FundAccountDraftRepository {
    String saveDraft(FundAccountSaveDraftPojo draft);
    List<FundAccountSaveDraftPojo> listDrafts(String corpid, int limit);
    FundAccountSaveDraftPojo loadDraft(String corpid, String draftCode);
    void removeDraft(String corpid, String draftCode);
}
