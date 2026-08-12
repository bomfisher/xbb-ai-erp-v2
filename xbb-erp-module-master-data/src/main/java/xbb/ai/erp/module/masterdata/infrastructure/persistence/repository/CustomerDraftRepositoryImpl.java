package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.masterdata.application.pojo.CustomerSaveDraftPojo;
import xbb.ai.erp.module.masterdata.application.port.CustomerDraftRepository;

@Repository
public class CustomerDraftRepositoryImpl implements CustomerDraftRepository {
    public String saveDraft(CustomerSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<CustomerSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public CustomerSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
