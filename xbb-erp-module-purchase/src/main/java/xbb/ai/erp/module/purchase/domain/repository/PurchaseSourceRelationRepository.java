package xbb.ai.erp.module.purchase.domain.repository;

import xbb.ai.erp.module.purchase.domain.model.PurchaseSourceRelation;

import java.util.List;
import java.util.Map;

public interface PurchaseSourceRelationRepository {
    void insert(PurchaseSourceRelation purchaseSourceRelation);

    void insertBatch(List<PurchaseSourceRelation> purchaseSourceRelationList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(PurchaseSourceRelation purchaseSourceRelation);

    PurchaseSourceRelation findById(String corpid, Long id);

    List<PurchaseSourceRelation> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
