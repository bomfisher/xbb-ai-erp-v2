package xbb.ai.erp.module.purchase.domain.repository;

import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;

import java.util.List;
import java.util.Map;

public interface PurchaseInboundRepository {
    void insert(PurchaseInbound purchaseInbound);

    void insertBatch(List<PurchaseInbound> purchaseInboundList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(PurchaseInbound purchaseInbound);

    PurchaseInbound findById(String corpid, Long id);

    List<PurchaseInbound> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
