package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.masterdata.domain.model.ProductSpu;
import xbb.ai.erp.module.masterdata.domain.repository.ProductSpuRepository;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor.ProductSpuConvertor;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper.ProductSpuMapper;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.ProductSpuPO;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.SupplierPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleMasterdataProductSpuRepositoryImpl")
@RequiredArgsConstructor
public class ProductSpuRepositoryImpl implements ProductSpuRepository {

    private final ProductSpuMapper productSpuMapper;

    @Override
    public Long insert(ProductSpu productSpu) {
        ProductSpuPO po = ProductSpuConvertor.toPO(productSpu);
        initializeForInsert(po);
        productSpuMapper.insert(po);
        productSpu.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<ProductSpu> productSpuList) {
        List<ProductSpuPO> poList = productSpuList.stream().map(ProductSpuConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        productSpuMapper.insertBatch(poList);
        for (int index = 0; index < productSpuList.size(); index++) {
            productSpuList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        productSpuMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        productSpuMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(ProductSpu productSpu) {
        ProductSpuPO po = ProductSpuConvertor.toPO(productSpu);
        productSpuMapper.update(po);
    }

    @Override
    public ProductSpu findById(String corpid, Long id) {
        return ProductSpuConvertor.toDomain(productSpuMapper.findById(corpid, id));
    }

    @Override
    public List<ProductSpu> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return productSpuMapper.findByCondition(preparedConditionMap).stream().map(ProductSpuConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return productSpuMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
