package xbb.ai.erp.module.purchase.domain.repository;

import xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem;

import java.util.List;
import java.util.Map;

public interface PurchaseRequestItemRepository {
    void insert(PurchaseRequestItem purchaseRequestItem);

    void insertBatch(List<PurchaseRequestItem> purchaseRequestItemList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(PurchaseRequestItem purchaseRequestItem);

    PurchaseRequestItem findById(String corpid, Long id);

    List<PurchaseRequestItem> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
