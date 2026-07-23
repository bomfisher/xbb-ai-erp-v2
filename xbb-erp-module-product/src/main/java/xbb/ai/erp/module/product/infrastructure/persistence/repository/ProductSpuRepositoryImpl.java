package xbb.ai.erp.module.product.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.idgen.IdGenerator;
import xbb.ai.erp.module.product.domain.model.ProductSpu;
import xbb.ai.erp.module.product.domain.repository.ProductSpuRepository;
import xbb.ai.erp.module.product.infrastructure.persistence.mapper.ProductSpuMapper;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductSpuPO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductSpuRepositoryImpl implements ProductSpuRepository {

    private final ProductSpuMapper productSpuMapper;
    private final IdGenerator idGenerator;

    @Override
    public Long save(ProductSpu productSpu, String userId) {
        long now = System.currentTimeMillis();
        ProductSpuPO po = toPO(productSpu);
        po.setId(idGenerator.nextId());
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
        po.setCreatorId(userId);
        po.setModifyId(userId);
        productSpuMapper.insert(po);
        return po.getId();
    }

    @Override
    public void update(ProductSpu productSpu, String userId) {
        ProductSpuPO po = toPO(productSpu);
        po.setModifyId(userId);
        po.setUpdateTime(System.currentTimeMillis());
        productSpuMapper.update(po);
    }

    @Override
    public void removeById(String corpid, Long id, String userId) {
        productSpuMapper.removeById(corpid, id, userId, System.currentTimeMillis());
    }

    @Override
    public ProductSpu findById(String corpid, Long id) {
        return toDomain(productSpuMapper.findById(corpid, id));
    }

    @Override
    public List<ProductSpu> findByCondition(Map<String, Object> condition) {
        return productSpuMapper.findByCondition(condition).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public long count(Map<String, Object> condition) {
        return productSpuMapper.count(condition);
    }

    private ProductSpuPO toPO(ProductSpu productSpu) {
        if (productSpu == null) {
            return null;
        }
        ProductSpuPO po = new ProductSpuPO();
        po.setId(productSpu.getId());
        po.setCorpid(productSpu.getCorpid());
        po.setSpuCode(productSpu.getSpuCode());
        po.setSpuName(productSpu.getSpuName());
        po.setCategoryId(productSpu.getCategoryId());
        po.setBrandId(productSpu.getBrandId());
        po.setProductType(productSpu.getProductType());
        po.setEnableSpec(productSpu.getEnableSpec());
        po.setDescription(productSpu.getDescription());
        po.setImageUrl(productSpu.getImageUrl());
        po.setEnableStatus(productSpu.getEnableStatus());
        return po;
    }

    private ProductSpu toDomain(ProductSpuPO po) {
        if (po == null) {
            return null;
        }
        ProductSpu productSpu = new ProductSpu();
        productSpu.setId(po.getId());
        productSpu.setCorpid(po.getCorpid());
        productSpu.setSpuCode(po.getSpuCode());
        productSpu.setSpuName(po.getSpuName());
        productSpu.setCategoryId(po.getCategoryId());
        productSpu.setBrandId(po.getBrandId());
        productSpu.setProductType(po.getProductType());
        productSpu.setEnableSpec(po.getEnableSpec());
        productSpu.setDescription(po.getDescription());
        productSpu.setImageUrl(po.getImageUrl());
        productSpu.setEnableStatus(po.getEnableStatus());
        return productSpu;
    }
}
