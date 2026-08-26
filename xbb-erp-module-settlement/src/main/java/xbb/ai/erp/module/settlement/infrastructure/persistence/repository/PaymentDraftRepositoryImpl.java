package xbb.ai.erp.module.settlement.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.settlement.application.pojo.PaymentSaveDraftPojo;
import xbb.ai.erp.module.settlement.application.port.PaymentDraftRepository;

@Repository
public class PaymentDraftRepositoryImpl implements PaymentDraftRepository {
    public String saveDraft(PaymentSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<PaymentSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public PaymentSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
