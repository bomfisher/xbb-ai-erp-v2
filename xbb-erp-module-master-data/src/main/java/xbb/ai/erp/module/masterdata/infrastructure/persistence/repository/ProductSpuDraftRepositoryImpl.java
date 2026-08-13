package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import java.util.List;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.masterdata.application.pojo.ProductSpuSaveDraftPojo;
import xbb.ai.erp.module.masterdata.application.port.ProductSpuDraftRepository;

@Repository
public class ProductSpuDraftRepositoryImpl implements ProductSpuDraftRepository {
    public String saveDraft(ProductSpuSaveDraftPojo draft) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public List<ProductSpuSaveDraftPojo> listDrafts(String corpid, int limit) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public ProductSpuSaveDraftPojo loadDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
    public void removeDraft(String corpid, String draftCode) { throw new UnsupportedOperationException("请配置草稿缓存实现"); }
}
