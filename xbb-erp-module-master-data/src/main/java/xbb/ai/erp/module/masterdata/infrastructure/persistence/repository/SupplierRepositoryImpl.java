package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.masterdata.domain.model.Supplier;
import xbb.ai.erp.module.masterdata.domain.repository.SupplierRepository;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor.SupplierConvertor;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper.SupplierMapper;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.SupplierPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleMasterdataSupplierRepositoryImpl")
@RequiredArgsConstructor
public class SupplierRepositoryImpl implements SupplierRepository {

    private final SupplierMapper supplierMapper;

    @Override
    public Long insert(Supplier supplier) {
        SupplierPO po = SupplierConvertor.toPO(supplier);
        initializeForInsert(po);
        po.setId(null);
        supplierMapper.insert(po);
        supplier.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<Supplier> supplierList) {
        List<SupplierPO> poList = supplierList.stream().map(SupplierConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        supplierMapper.insertBatch(poList);
        for (int index = 0; index < supplierList.size(); index++) {
            supplierList.get(index).setId(poList.get(index).getId());
        }
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
        SupplierPO po = SupplierConvertor.toPO(supplier);
        po.setUpdateTime(System.currentTimeMillis());
        supplierMapper.update(po);
    }

    @Override
    public Supplier findById(String corpid, Long id) {
        return SupplierConvertor.toDomain(supplierMapper.findById(corpid, id));
    }

    @Override
    public List<Supplier> findByIds(String corpid, java.util.Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return supplierMapper.findByIds(corpid, ids).stream().map(SupplierConvertor::toDomain).toList();
    }

    @Override
    public List<Supplier> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return supplierMapper.findByCondition(preparedConditionMap).stream().map(SupplierConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return supplierMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(SupplierPO po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
