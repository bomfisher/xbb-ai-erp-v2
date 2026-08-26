package xbb.ai.erp.module.settlement.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.settlement.application.pojo.ReceiptSaveDraftPojo;
import xbb.ai.erp.module.settlement.application.port.ReceiptDraftRepository;

@Repository
public class ReceiptDraftRepositoryImpl implements ReceiptDraftRepository {
    public String saveDraft(ReceiptSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<ReceiptSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public ReceiptSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
