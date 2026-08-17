package xbb.ai.erp.module.sales.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.sales.application.pojo.SalesOrderSaveDraftPojo;
import xbb.ai.erp.module.sales.application.port.SalesOrderDraftRepository;

@Repository
public class SalesOrderDraftRepositoryImpl implements SalesOrderDraftRepository {
    public String saveDraft(SalesOrderSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<SalesOrderSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public SalesOrderSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
