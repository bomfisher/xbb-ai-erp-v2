package xbb.ai.erp.module.purchase.domain.repository;

import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;

import java.util.List;
import java.util.Map;

public interface PurchaseOrderItemRepository {
    Long insert(PurchaseOrderItem purchaseOrderItem);

    void insertBatch(List<PurchaseOrderItem> purchaseOrderItemList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(PurchaseOrderItem purchaseOrderItem);

    PurchaseOrderItem findById(String corpid, Long id);

    List<PurchaseOrderItem> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
