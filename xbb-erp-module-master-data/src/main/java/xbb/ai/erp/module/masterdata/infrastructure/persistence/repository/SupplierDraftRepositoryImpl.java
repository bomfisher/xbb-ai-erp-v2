package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.masterdata.application.pojo.SupplierSaveDraftPojo;
import xbb.ai.erp.module.masterdata.application.port.SupplierDraftRepository;

@Repository
public class SupplierDraftRepositoryImpl implements SupplierDraftRepository {
    public String saveDraft(SupplierSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<SupplierSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public SupplierSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
