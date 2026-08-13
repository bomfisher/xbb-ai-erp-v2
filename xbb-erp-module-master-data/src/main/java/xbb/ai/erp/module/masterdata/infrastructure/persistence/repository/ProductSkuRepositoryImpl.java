package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.masterdata.domain.model.ProductSku;
import xbb.ai.erp.module.masterdata.domain.repository.ProductSkuRepository;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor.ProductSkuConvertor;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper.ProductSkuMapper;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.ProductSkuPO;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.SupplierPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleMasterdataProductSkuRepositoryImpl")
@RequiredArgsConstructor
public class ProductSkuRepositoryImpl implements ProductSkuRepository {

    private final ProductSkuMapper productSkuMapper;

    @Override
    public Long insert(ProductSku productSku) {
        ProductSkuPO po = ProductSkuConvertor.toPO(productSku);
        initializeForInsert(po);
        productSkuMapper.insert(po);
        productSku.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<ProductSku> productSkuList) {
        List<ProductSkuPO> poList = productSkuList.stream().map(ProductSkuConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        productSkuMapper.insertBatch(poList);
        for (int index = 0; index < productSkuList.size(); index++) {
            productSkuList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        productSkuMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        productSkuMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(ProductSku productSku) {
        ProductSkuPO po = ProductSkuConvertor.toPO(productSku);
        productSkuMapper.update(po);
    }

    @Override
    public ProductSku findById(String corpid, Long id) {
        return ProductSkuConvertor.toDomain(productSkuMapper.findById(corpid, id));
    }

    @Override
    public List<ProductSku> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return productSkuMapper.findByCondition(preparedConditionMap).stream().map(ProductSkuConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return productSkuMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
