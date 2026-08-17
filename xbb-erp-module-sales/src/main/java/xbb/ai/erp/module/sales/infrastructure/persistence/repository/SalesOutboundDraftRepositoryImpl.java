package xbb.ai.erp.module.sales.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.sales.application.pojo.SalesOutboundSaveDraftPojo;
import xbb.ai.erp.module.sales.application.port.SalesOutboundDraftRepository;

@Repository
public class SalesOutboundDraftRepositoryImpl implements SalesOutboundDraftRepository {
    public String saveDraft(SalesOutboundSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<SalesOutboundSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public SalesOutboundSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
