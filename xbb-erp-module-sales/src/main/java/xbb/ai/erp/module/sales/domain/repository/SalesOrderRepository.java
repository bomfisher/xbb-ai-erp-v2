package xbb.ai.erp.module.sales.domain.repository;

import xbb.ai.erp.module.sales.domain.model.SalesOrder;

import java.util.List;
import java.util.Map;

public interface SalesOrderRepository {
    Long insert(SalesOrder salesOrder);

    void insertBatch(List<SalesOrder> salesOrderList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(SalesOrder salesOrder);

    SalesOrder findById(String corpid, Long id);

    List<SalesOrder> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
