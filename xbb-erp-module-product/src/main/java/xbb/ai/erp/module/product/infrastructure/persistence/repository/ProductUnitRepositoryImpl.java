package xbb.ai.erp.module.product.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.idgen.IdGenerator;
import xbb.ai.erp.module.product.domain.model.ProductUnit;
import xbb.ai.erp.module.product.domain.repository.ProductUnitRepository;
import xbb.ai.erp.module.product.infrastructure.persistence.mapper.ProductUnitMapper;
import xbb.ai.erp.module.product.infrastructure.persistence.po.ProductUnitPO;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class ProductUnitRepositoryImpl implements ProductUnitRepository {

    private final ProductUnitMapper productUnitMapper;
    private final IdGenerator idGenerator;

    @Override
    public Long save(ProductUnit productUnit, String userId) {
        long now = System.currentTimeMillis();
        ProductUnitPO po = toPO(productUnit);
        po.setId(idGenerator.nextId());
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
        po.setCreatorId(userId);
        po.setModifyId(userId);
        productUnitMapper.insert(po);
        return po.getId();
    }

    @Override
    public void update(ProductUnit productUnit, String userId) {
        ProductUnitPO po = toPO(productUnit);
        po.setModifyId(userId);
        po.setUpdateTime(System.currentTimeMillis());
        productUnitMapper.update(po);
    }

    @Override
    public void removeById(String corpid, Long id, String userId) {
        productUnitMapper.removeById(corpid, id, userId, System.currentTimeMillis());
    }

    @Override
    public ProductUnit findById(String corpid, Long id) {
        return toDomain(productUnitMapper.findById(corpid, id));
    }

    @Override
    public List<ProductUnit> findByCondition(Map<String, Object> condition) {
        return productUnitMapper.findByCondition(condition).stream().map(this::toDomain).collect(Collectors.toList());
    }

    @Override
    public long count(Map<String, Object> condition) {
        return productUnitMapper.count(condition);
    }

    private ProductUnitPO toPO(ProductUnit productUnit) {
        if (productUnit == null) {
            return null;
        }
        ProductUnitPO po = new ProductUnitPO();
        po.setId(productUnit.getId());
        po.setCorpid(productUnit.getCorpid());
        po.setUnitCode(productUnit.getUnitCode());
        po.setUnitName(productUnit.getUnitName());
        po.setPrecisionNum(productUnit.getPrecisionNum());
        po.setEnableStatus(productUnit.getEnableStatus());
        return po;
    }

    private ProductUnit toDomain(ProductUnitPO po) {
        if (po == null) {
            return null;
        }
        ProductUnit productUnit = new ProductUnit();
        productUnit.setId(po.getId());
        productUnit.setCorpid(po.getCorpid());
        productUnit.setUnitCode(po.getUnitCode());
        productUnit.setUnitName(po.getUnitName());
        productUnit.setPrecisionNum(po.getPrecisionNum());
        productUnit.setEnableStatus(po.getEnableStatus());
        return productUnit;
    }
}
