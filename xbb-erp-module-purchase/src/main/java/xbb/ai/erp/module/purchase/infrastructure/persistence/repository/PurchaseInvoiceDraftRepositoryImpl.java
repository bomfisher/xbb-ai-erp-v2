package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseInvoiceSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.port.PurchaseInvoiceDraftRepository;

@Repository
public class PurchaseInvoiceDraftRepositoryImpl implements PurchaseInvoiceDraftRepository {
    public String saveDraft(PurchaseInvoiceSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<PurchaseInvoiceSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public PurchaseInvoiceSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
