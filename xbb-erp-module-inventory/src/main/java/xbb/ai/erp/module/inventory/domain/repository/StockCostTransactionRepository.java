package xbb.ai.erp.module.inventory.domain.repository;

import xbb.ai.erp.module.inventory.domain.model.StockCostTransaction;

import java.util.List;
import java.util.Map;

public interface StockCostTransactionRepository {
    Long insert(StockCostTransaction stockCostTransaction);

    void insertBatch(List<StockCostTransaction> stockCostTransactionList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(StockCostTransaction stockCostTransaction);

    StockCostTransaction findById(String corpid, Long id);

    StockCostTransaction findByIdempotencyKey(String corpid, String idempotencyKey);

    List<StockCostTransaction> findByIdempotencyKeys(String corpid, List<String> idempotencyKeys);

    List<StockCostTransaction> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
