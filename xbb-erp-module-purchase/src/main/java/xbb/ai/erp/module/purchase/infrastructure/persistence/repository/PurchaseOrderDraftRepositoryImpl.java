package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.port.PurchaseOrderDraftRepository;

@Repository
public class PurchaseOrderDraftRepositoryImpl implements PurchaseOrderDraftRepository {
    public String saveDraft(PurchaseOrderSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<PurchaseOrderSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public PurchaseOrderSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
