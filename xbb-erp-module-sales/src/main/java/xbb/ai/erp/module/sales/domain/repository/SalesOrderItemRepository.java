package xbb.ai.erp.module.sales.domain.repository;

import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;

import java.util.List;
import java.util.Map;

public interface SalesOrderItemRepository {
    Long insert(SalesOrderItem salesOrderItem);

    void insertBatch(List<SalesOrderItem> salesOrderItemList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(SalesOrderItem salesOrderItem);

    SalesOrderItem findById(String corpid, Long id);

    List<SalesOrderItem> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
