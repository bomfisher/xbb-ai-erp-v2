package xbb.ai.erp.module.demo.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.demo.application.pojo.DemoSaveDraftPojo;
import xbb.ai.erp.module.demo.application.port.DemoDraftRepository;

@Repository
public class DemoDraftRepositoryImpl implements DemoDraftRepository {
    public String saveDraft(DemoSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<DemoSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public DemoSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
