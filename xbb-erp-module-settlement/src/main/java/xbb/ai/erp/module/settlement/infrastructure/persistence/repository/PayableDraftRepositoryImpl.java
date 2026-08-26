package xbb.ai.erp.module.settlement.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.settlement.application.pojo.PayableSaveDraftPojo;
import xbb.ai.erp.module.settlement.application.port.PayableDraftRepository;

@Repository
public class PayableDraftRepositoryImpl implements PayableDraftRepository {
    public String saveDraft(PayableSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<PayableSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public PayableSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
