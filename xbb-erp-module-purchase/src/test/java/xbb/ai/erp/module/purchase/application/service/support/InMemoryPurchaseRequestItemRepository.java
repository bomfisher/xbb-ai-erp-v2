package xbb.ai.erp.module.purchase.application.service.support;

import xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPurchaseRequestItemRepository implements PurchaseRequestItemRepository {

    private final List<PurchaseRequestItem> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1L);

    public List<PurchaseRequestItem> all() {
        return data;
    }

    @Override
    public void insert(PurchaseRequestItem purchaseRequestItem) {
        if (purchaseRequestItem.getId() == null) {
            purchaseRequestItem.setId(sequence.getAndIncrement());
        }
        data.add(purchaseRequestItem);
    }

    @Override
    public void insertBatch(List<PurchaseRequestItem> purchaseRequestItemList) {
        purchaseRequestItemList.forEach(this::insert);
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
    public void update(PurchaseRequestItem purchaseRequestItem) {
        removeById(purchaseRequestItem.getCorpid(), purchaseRequestItem.getId());
        data.add(purchaseRequestItem);
    }

    @Override
    public PurchaseRequestItem findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<PurchaseRequestItem> findByCondition(Map<String, Object> conditionMap) {
        return data;
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return (long) data.size();
    }
}
