package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.masterdata.application.pojo.WarehouseSaveDraftPojo;
import xbb.ai.erp.module.masterdata.application.port.WarehouseDraftRepository;

@Repository
public class WarehouseDraftRepositoryImpl implements WarehouseDraftRepository {
    public String saveDraft(WarehouseSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<WarehouseSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public WarehouseSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
