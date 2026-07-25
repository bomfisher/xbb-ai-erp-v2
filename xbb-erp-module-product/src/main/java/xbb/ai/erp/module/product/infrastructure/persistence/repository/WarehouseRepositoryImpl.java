package xbb.ai.erp.module.product.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.idgen.IdGenerator;
import xbb.ai.erp.module.product.domain.model.Warehouse;
import xbb.ai.erp.module.product.domain.repository.WarehouseRepository;
import xbb.ai.erp.module.product.infrastructure.persistence.convertor.WarehouseConvertor;
import xbb.ai.erp.module.product.infrastructure.persistence.mapper.WarehouseMapper;
import xbb.ai.erp.module.product.infrastructure.persistence.po.WarehousePO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class WarehouseRepositoryImpl implements WarehouseRepository {

    private final WarehouseMapper warehouseMapper;
    private final IdGenerator idGenerator;

    @Override
    public void insert(Warehouse warehouse) {
        WarehousePO po = WarehouseConvertor.toPO(warehouse);
        if (po.getId() == null) {
            po.setId(idGenerator.nextId());
        }
        warehouse.setId(po.getId());
        if (po.getDel() == null) {
            po.setDel(0);
        }
        warehouseMapper.insert(po);
    }

    @Override
    public void insertBatch(List<Warehouse> warehouseList) {
        List<WarehousePO> poList = warehouseList.stream().map(warehouse -> {
            WarehousePO po = WarehouseConvertor.toPO(warehouse);
            if (po.getId() == null) {
                po.setId(idGenerator.nextId());
                warehouse.setId(po.getId());
            }
            if (po.getDel() == null) {
                po.setDel(0);
            }
            return po;
        }).toList();
        warehouseMapper.insertBatch(poList);
    }

    @Override
    public void removeById(String corpid, Long id) {
        warehouseMapper.removeById(corpid, id, null, System.currentTimeMillis());
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        warehouseMapper.removeBatchByIds(corpid, ids, null, System.currentTimeMillis());
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
        return warehouseMapper.findByCondition(WarehouseConditionMapHelper.prepare(conditionMap)).stream().map(WarehouseConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return warehouseMapper.count(WarehouseConditionMapHelper.prepare(conditionMap));
    }
}
