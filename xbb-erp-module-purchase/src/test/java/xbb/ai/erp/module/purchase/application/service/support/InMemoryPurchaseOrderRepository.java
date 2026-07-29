package xbb.ai.erp.module.purchase.application.service.support;

import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPurchaseOrderRepository implements PurchaseOrderRepository {

    private final List<PurchaseOrder> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1L);

    public List<PurchaseOrder> all() {
        return data;
    }

    @Override
    public void insert(PurchaseOrder purchaseOrder) {
        if (purchaseOrder.getId() == null) {
            purchaseOrder.setId(sequence.getAndIncrement());
        }
        data.add(purchaseOrder);
    }

    @Override
    public void insertBatch(List<PurchaseOrder> purchaseOrderList) {
        purchaseOrderList.forEach(this::insert);
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
    public void update(PurchaseOrder purchaseOrder) {
        removeById(purchaseOrder.getCorpid(), purchaseOrder.getId());
        data.add(purchaseOrder);
    }

    @Override
    public PurchaseOrder findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<PurchaseOrder> findByCondition(Map<String, Object> conditionMap) {
        return data;
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return (long) data.size();
    }
}
