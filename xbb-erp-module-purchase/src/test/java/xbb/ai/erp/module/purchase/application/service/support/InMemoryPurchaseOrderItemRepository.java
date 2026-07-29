package xbb.ai.erp.module.purchase.application.service.support;

import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPurchaseOrderItemRepository implements PurchaseOrderItemRepository {

    private final List<PurchaseOrderItem> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1L);

    public List<PurchaseOrderItem> all() {
        return data;
    }

    @Override
    public void insert(PurchaseOrderItem purchaseOrderItem) {
        if (purchaseOrderItem.getId() == null) {
            purchaseOrderItem.setId(sequence.getAndIncrement());
        }
        data.add(purchaseOrderItem);
    }

    @Override
    public void insertBatch(List<PurchaseOrderItem> purchaseOrderItemList) {
        purchaseOrderItemList.forEach(this::insert);
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
    public void update(PurchaseOrderItem purchaseOrderItem) {
        removeById(purchaseOrderItem.getCorpid(), purchaseOrderItem.getId());
        data.add(purchaseOrderItem);
    }

    @Override
    public PurchaseOrderItem findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<PurchaseOrderItem> findByCondition(Map<String, Object> conditionMap) {
        return data;
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return (long) data.size();
    }
}
