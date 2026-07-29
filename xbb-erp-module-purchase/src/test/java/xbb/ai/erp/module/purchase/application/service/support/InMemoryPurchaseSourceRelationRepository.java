package xbb.ai.erp.module.purchase.application.service.support;

import xbb.ai.erp.module.purchase.domain.model.PurchaseSourceRelation;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseSourceRelationRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPurchaseSourceRelationRepository implements PurchaseSourceRelationRepository {

    private final List<PurchaseSourceRelation> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1L);

    public List<PurchaseSourceRelation> all() {
        return data;
    }

    @Override
    public void insert(PurchaseSourceRelation purchaseSourceRelation) {
        if (purchaseSourceRelation.getId() == null) {
            purchaseSourceRelation.setId(sequence.getAndIncrement());
        }
        data.add(purchaseSourceRelation);
    }

    @Override
    public void insertBatch(List<PurchaseSourceRelation> purchaseSourceRelationList) {
        purchaseSourceRelationList.forEach(this::insert);
    }

    @Override
    public void removeById(String corpid, Long id) {
        data.removeIf(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId()));
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        data.removeIf(item -> corpid.equals(item.getCorpid()) && ids.contains(item.getId()));
    }

    @Override
    public void update(PurchaseSourceRelation purchaseSourceRelation) {
        removeById(purchaseSourceRelation.getCorpid(), purchaseSourceRelation.getId());
        data.add(purchaseSourceRelation);
    }

    @Override
    public PurchaseSourceRelation findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<PurchaseSourceRelation> findByCondition(Map<String, Object> conditionMap) {
        return data;
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return (long) data.size();
    }
}
