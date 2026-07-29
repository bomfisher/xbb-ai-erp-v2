package xbb.ai.erp.module.purchase.application.service.support;

import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPurchaseRequestRepository implements PurchaseRequestRepository {

    private final List<PurchaseRequest> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1L);

    public List<PurchaseRequest> all() {
        return data;
    }

    @Override
    public void insert(PurchaseRequest purchaseRequest) {
        if (purchaseRequest.getId() == null) {
            purchaseRequest.setId(sequence.getAndIncrement());
        }
        data.add(purchaseRequest);
    }

    @Override
    public void insertBatch(List<PurchaseRequest> purchaseRequestList) {
        purchaseRequestList.forEach(this::insert);
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
    public void update(PurchaseRequest purchaseRequest) {
        removeById(purchaseRequest.getCorpid(), purchaseRequest.getId());
        data.add(purchaseRequest);
    }

    @Override
    public PurchaseRequest findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<PurchaseRequest> findByCondition(Map<String, Object> conditionMap) {
        return data;
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return (long) data.size();
    }
}
