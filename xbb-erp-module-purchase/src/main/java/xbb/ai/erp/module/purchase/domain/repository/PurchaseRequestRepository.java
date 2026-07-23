package xbb.ai.erp.module.purchase.domain.repository;

import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;

import java.util.List;
import java.util.Map;

public interface PurchaseRequestRepository {
    void insert(PurchaseRequest purchaseRequest);

    void insertBatch(List<PurchaseRequest> purchaseRequestList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(PurchaseRequest purchaseRequest);

    PurchaseRequest findById(String corpid, Long id);

    List<PurchaseRequest> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
