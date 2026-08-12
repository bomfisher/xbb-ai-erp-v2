package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.masterdata.domain.model.Warehouse;
import xbb.ai.erp.module.masterdata.domain.repository.WarehouseRepository;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor.WarehouseConvertor;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper.WarehouseMapper;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.WarehousePO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleMasterdataWarehouseRepositoryImpl")
@RequiredArgsConstructor
public class WarehouseRepositoryImpl implements WarehouseRepository {

    private final WarehouseMapper warehouseMapper;

    @Override
    public Long insert(Warehouse warehouse) {
        WarehousePO po = WarehouseConvertor.toPO(warehouse);
        po.setId(null);
        warehouseMapper.insert(po);
        warehouse.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<Warehouse> warehouseList) {
        List<WarehousePO> poList = warehouseList.stream().map(WarehouseConvertor::toPO).toList();
        poList.forEach(po -> po.setId(null));
        warehouseMapper.insertBatch(poList);
        for (int index = 0; index < warehouseList.size(); index++) {
            warehouseList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        warehouseMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        warehouseMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(Warehouse warehouse) {
        WarehousePO po = WarehouseConvertor.toPO(warehouse);
        warehouseMapper.update(po);
    }

    @Override
    public Warehouse findById(String corpid, Long id) {
        return WarehouseConvertor.toDomain(warehouseMapper.findById(corpid, id));
    }

    @Override
    public List<Warehouse> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return warehouseMapper.findByCondition(preparedConditionMap).stream().map(WarehouseConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return warehouseMapper.count(preparedConditionMap);
    }
}
