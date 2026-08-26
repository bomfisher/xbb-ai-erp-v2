package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.masterdata.application.pojo.FundAccountSaveDraftPojo;
import xbb.ai.erp.module.masterdata.application.port.FundAccountDraftRepository;

@Repository
public class FundAccountDraftRepositoryImpl implements FundAccountDraftRepository {
    public String saveDraft(FundAccountSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<FundAccountSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public FundAccountSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
