package xbb.ai.erp.module.inventory.domain.repository;

import xbb.ai.erp.module.inventory.domain.model.StockBalance;

import java.util.List;
import java.util.Map;

public interface StockBalanceRepository {
    Long insert(StockBalance stockBalance);

    void insertBatch(List<StockBalance> stockBalanceList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(StockBalance stockBalance);

    StockBalance findById(String corpid, Long id);

    StockBalance findByWarehouseAndSku(String corpid, Long warehouseId, Long skuId);

    List<StockBalance> findByWarehouseAndSkuPairs(String corpid, List<StockBalance> stockKeys);

    List<StockBalance> findByWarehouseAndSkuPairsForUpdate(String corpid, List<StockBalance> stockKeys);

    boolean updateWithVersion(StockBalance stockBalance, Integer expectedVersion);

    List<StockBalance> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
