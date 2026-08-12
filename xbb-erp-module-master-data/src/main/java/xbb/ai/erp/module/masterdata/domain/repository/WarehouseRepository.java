package xbb.ai.erp.module.masterdata.domain.repository;

import xbb.ai.erp.module.masterdata.domain.model.Warehouse;

import java.util.List;
import java.util.Map;

public interface WarehouseRepository {
    Long insert(Warehouse warehouse);

    void insertBatch(List<Warehouse> warehouseList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(Warehouse warehouse);

    Warehouse findById(String corpid, Long id);

    List<Warehouse> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
