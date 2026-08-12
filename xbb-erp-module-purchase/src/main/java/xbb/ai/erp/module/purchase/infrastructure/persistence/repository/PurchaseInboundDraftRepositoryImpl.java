package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseInboundSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.port.PurchaseInboundDraftRepository;

@Repository
public class PurchaseInboundDraftRepositoryImpl implements PurchaseInboundDraftRepository {
    public String saveDraft(PurchaseInboundSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<PurchaseInboundSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public PurchaseInboundSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
