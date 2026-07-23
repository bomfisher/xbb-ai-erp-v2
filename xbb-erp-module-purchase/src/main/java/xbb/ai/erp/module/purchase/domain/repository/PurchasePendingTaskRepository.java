package xbb.ai.erp.module.purchase.domain.repository;

import xbb.ai.erp.module.purchase.domain.model.PurchasePendingTask;

import java.util.List;
import java.util.Map;

public interface PurchasePendingTaskRepository {
    void insert(PurchasePendingTask purchasePendingTask);

    void insertBatch(List<PurchasePendingTask> purchasePendingTaskList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(PurchasePendingTask purchasePendingTask);

    PurchasePendingTask findById(String corpid, Long id);

    List<PurchasePendingTask> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
