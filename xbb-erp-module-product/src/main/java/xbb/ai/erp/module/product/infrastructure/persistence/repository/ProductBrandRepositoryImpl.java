package xbb.ai.erp.module.product.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.idgen.IdGenerator;
import xbb.ai.erp.module.product.domain.model.ProductBrand;
import xbb.ai.erp.module.product.domain.repository.ProductBrandRepository;
import xbb.ai.erp.module.product.infrastructure.persistence.mapper.ProductBrandMapper;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductBrandPO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductBrandRepositoryImpl implements ProductBrandRepository {

    private final ProductBrandMapper productBrandMapper;
    private final IdGenerator idGenerator;

    @Override
    public Long save(ProductBrand productBrand, String userId) {
        long now = System.currentTimeMillis();
        ProductBrandPO po = toPO(productBrand);
        po.setId(idGenerator.nextId());
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
        po.setCreatorId(userId);
        po.setModifyId(userId);
        productBrandMapper.insert(po);
        return po.getId();
    }

    @Override
    public void update(ProductBrand productBrand, String userId) {
        ProductBrandPO po = toPO(productBrand);
        po.setModifyId(userId);
        po.setUpdateTime(System.currentTimeMillis());
        productBrandMapper.update(po);
    }

    @Override
    public void removeById(String corpid, Long id, String userId) {
        productBrandMapper.removeById(corpid, id, userId, System.currentTimeMillis());
    }

    @Override
    public ProductBrand findById(String corpid, Long id) {
        return toDomain(productBrandMapper.findById(corpid, id));
    }

    @Override
    public List<ProductBrand> findByCondition(Map<String, Object> condition) {
        return productBrandMapper.findByCondition(condition).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public long count(Map<String, Object> condition) {
        return productBrandMapper.count(condition);
    }

    private ProductBrandPO toPO(ProductBrand productBrand) {
        if (productBrand == null) {
            return null;
        }
        ProductBrandPO po = new ProductBrandPO();
        po.setId(productBrand.getId());
        po.setCorpid(productBrand.getCorpid());
        po.setBrandCode(productBrand.getBrandCode());
        po.setBrandName(productBrand.getBrandName());
        po.setSortNo(productBrand.getSortNo());
        po.setEnableStatus(productBrand.getEnableStatus());
        return po;
    }

    private ProductBrand toDomain(ProductBrandPO po) {
        if (po == null) {
            return null;
        }
        ProductBrand productBrand = new ProductBrand();
        productBrand.setId(po.getId());
        productBrand.setCorpid(po.getCorpid());
        productBrand.setBrandCode(po.getBrandCode());
        productBrand.setBrandName(po.getBrandName());
        productBrand.setSortNo(po.getSortNo());
        productBrand.setEnableStatus(po.getEnableStatus());
        return productBrand;
    }
}
