package xbb.ai.erp.module.product.application.service.support;

import xbb.ai.erp.module.product.domain.model.Warehouse;
import xbb.ai.erp.module.product.domain.repository.WarehouseRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeWarehouseRepository implements WarehouseRepository {

    private final List<Warehouse> data;

    public FakeWarehouseRepository(List<Warehouse> data) {
        this.data = new ArrayList<>(data);
    }

    @Override
    public void insert(Warehouse warehouse) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBatch(List<Warehouse> warehouseList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeById(String corpid, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Warehouse warehouse) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Warehouse findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Warehouse> findByCondition(Map<String, Object> conditionMap) {
        Object corpid = conditionMap.get("corpid");
        Object id = conditionMap.get("id");
        Object warehouseCode = conditionMap.get("warehouseCode");
        Object warehouseName = conditionMap.get("warehouseName");
        return data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (id == null || id.equals(item.getId()))
                && (warehouseCode == null || warehouseCode.equals(item.getWarehouseCode()))
                && (warehouseName == null || warehouseName.equals(item.getWarehouseName()))
        ).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return (long) findByCondition(conditionMap).size();
    }
}
