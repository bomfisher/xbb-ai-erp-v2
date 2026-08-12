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
        po.setId(null);
        supplierMapper.insert(po);
        supplier.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<Supplier> supplierList) {
        List<SupplierPO> poList = supplierList.stream().map(SupplierConvertor::toPO).toList();
        poList.forEach(po -> po.setId(null));
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
        supplierMapper.update(po);
    }

    @Override
    public Supplier findById(String corpid, Long id) {
        return SupplierConvertor.toDomain(supplierMapper.findById(corpid, id));
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
}
