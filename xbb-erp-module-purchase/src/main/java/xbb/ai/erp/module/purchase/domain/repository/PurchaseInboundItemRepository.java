package xbb.ai.erp.module.purchase.domain.repository;

import xbb.ai.erp.module.purchase.domain.model.PurchaseInboundItem;

import java.util.List;
import java.util.Map;

public interface PurchaseInboundItemRepository {
    Long insert(PurchaseInboundItem purchaseInboundItem);

    void insertBatch(List<PurchaseInboundItem> purchaseInboundItemList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(PurchaseInboundItem purchaseInboundItem);

    PurchaseInboundItem findById(String corpid, Long id);

    List<PurchaseInboundItem> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
