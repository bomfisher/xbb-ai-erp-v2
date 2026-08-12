package xbb.ai.erp.module.purchase.domain.repository;

import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;

import java.util.List;
import java.util.Map;

public interface PurchaseOrderRepository {
    Long insert(PurchaseOrder purchaseOrder);

    void insertBatch(List<PurchaseOrder> purchaseOrderList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(PurchaseOrder purchaseOrder);

    PurchaseOrder findById(String corpid, Long id);

    List<PurchaseOrder> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
