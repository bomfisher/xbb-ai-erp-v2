package xbb.ai.erp.module.product.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.idgen.IdGenerator;
import xbb.ai.erp.module.product.domain.model.ProductCategory;
import xbb.ai.erp.module.product.domain.repository.ProductCategoryRepository;
import xbb.ai.erp.module.product.infrastructure.persistence.mapper.ProductCategoryMapper;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductCategoryPO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductCategoryRepositoryImpl implements ProductCategoryRepository {

    private final ProductCategoryMapper productCategoryMapper;
    private final IdGenerator idGenerator;

    @Override
    public Long save(ProductCategory productCategory, String userId) {
        long now = System.currentTimeMillis();
        ProductCategoryPO po = toPO(productCategory);
        po.setId(idGenerator.nextId());
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
        po.setCreatorId(userId);
        po.setModifyId(userId);
        productCategoryMapper.insert(po);
        return po.getId();
    }

    @Override
    public void update(ProductCategory productCategory, String userId) {
        ProductCategoryPO po = toPO(productCategory);
        po.setModifyId(userId);
        po.setUpdateTime(System.currentTimeMillis());
        productCategoryMapper.update(po);
    }

    @Override
    public void removeById(String corpid, Long id, String userId) {
        productCategoryMapper.removeById(corpid, id, userId, System.currentTimeMillis());
    }

    @Override
    public ProductCategory findById(String corpid, Long id) {
        return toDomain(productCategoryMapper.findById(corpid, id));
    }

    @Override
    public List<ProductCategory> findByCondition(Map<String, Object> condition) {
        return productCategoryMapper.findByCondition(condition).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public long count(Map<String, Object> condition) {
        return productCategoryMapper.count(condition);
    }

    private ProductCategoryPO toPO(ProductCategory productCategory) {
        if (productCategory == null) {
            return null;
        }
        ProductCategoryPO po = new ProductCategoryPO();
        po.setId(productCategory.getId());
        po.setCorpid(productCategory.getCorpid());
        po.setCategoryCode(productCategory.getCategoryCode());
        po.setCategoryName(productCategory.getCategoryName());
        po.setParentId(productCategory.getParentId());
        po.setCategoryLevel(productCategory.getCategoryLevel());
        po.setSortNo(productCategory.getSortNo());
        po.setEnableStatus(productCategory.getEnableStatus());
        return po;
    }

    private ProductCategory toDomain(ProductCategoryPO po) {
        if (po == null) {
            return null;
        }
        ProductCategory productCategory = new ProductCategory();
        productCategory.setId(po.getId());
        productCategory.setCorpid(po.getCorpid());
        productCategory.setCategoryCode(po.getCategoryCode());
        productCategory.setCategoryName(po.getCategoryName());
        productCategory.setParentId(po.getParentId());
        productCategory.setCategoryLevel(po.getCategoryLevel());
        productCategory.setSortNo(po.getSortNo());
        productCategory.setEnableStatus(po.getEnableStatus());
        return productCategory;
    }
}
