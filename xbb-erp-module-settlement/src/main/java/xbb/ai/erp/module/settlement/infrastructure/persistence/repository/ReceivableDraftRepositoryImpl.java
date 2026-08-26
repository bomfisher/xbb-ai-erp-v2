package xbb.ai.erp.module.settlement.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.settlement.application.pojo.ReceivableSaveDraftPojo;
import xbb.ai.erp.module.settlement.application.port.ReceivableDraftRepository;

@Repository
public class ReceivableDraftRepositoryImpl implements ReceivableDraftRepository {
    public String saveDraft(ReceivableSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<ReceivableSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public ReceivableSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
