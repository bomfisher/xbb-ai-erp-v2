package xbb.ai.erp.module.inventory.domain.repository;

import xbb.ai.erp.module.inventory.domain.model.StockTransaction;
import xbb.ai.erp.module.inventory.admin.dto.StockTransactionQueryDTO;
import xbb.ai.erp.module.inventory.domain.pojo.StockTransactionQueryPojo;

import java.util.List;
import java.util.Map;

public interface StockTransactionRepository {
    List<StockTransactionQueryPojo> queryList(StockTransactionQueryDTO query, int offset, int pageSize);
    Long queryCount(StockTransactionQueryDTO query);
    Long insert(StockTransaction stockTransaction);

    void insertBatch(List<StockTransaction> stockTransactionList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(StockTransaction stockTransaction);

    StockTransaction findById(String corpid, Long id);

    List<StockTransaction> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
