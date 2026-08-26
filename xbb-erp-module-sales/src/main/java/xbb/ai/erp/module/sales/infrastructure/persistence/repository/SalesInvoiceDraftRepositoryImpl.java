package xbb.ai.erp.module.sales.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.sales.application.pojo.SalesInvoiceSaveDraftPojo;
import xbb.ai.erp.module.sales.application.port.SalesInvoiceDraftRepository;

@Repository
public class SalesInvoiceDraftRepositoryImpl implements SalesInvoiceDraftRepository {
    public String saveDraft(SalesInvoiceSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<SalesInvoiceSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public SalesInvoiceSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
