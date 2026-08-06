package xbb.ai.erp.module.supplier.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.common.support.QueryConditionMapHelper;
import xbb.ai.erp.module.supplier.domain.model.Supplier;
import xbb.ai.erp.module.supplier.domain.repository.SupplierRepository;
import xbb.ai.erp.module.supplier.infrastructure.persistence.convertor.SupplierConvertor;
import xbb.ai.erp.module.supplier.infrastructure.persistence.mapper.SupplierMapper;
import xbb.ai.erp.module.supplier.infrastructure.persistence.po.SupplierPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class SupplierRepositoryImpl implements SupplierRepository {

    private final SupplierMapper supplierMapper;

    @Override
    public void insert(Supplier supplier) {
        SupplierPO po = SupplierConvertor.toPO(supplier);
        supplierMapper.insert(po);
        supplier.setId(po.getId());
    }

    @Override
    public void insertBatch(List<Supplier> supplierList) {
        supplierMapper.insertBatch(supplierList.stream().map(SupplierConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        supplierMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        supplierMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(Supplier supplier) {
        supplierMapper.update(SupplierConvertor.toPO(supplier));
    }

    @Override
    public Supplier findById(String corpid, Long id) {
        return SupplierConvertor.toDomain(supplierMapper.findById(corpid, id));
    }

    @Override
    public List<Supplier> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = QueryConditionMapHelper.prepare(conditionMap);
        return supplierMapper.findByCondition(preparedConditionMap).stream().map(SupplierConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return supplierMapper.count(QueryConditionMapHelper.prepare(conditionMap));
    }
}
