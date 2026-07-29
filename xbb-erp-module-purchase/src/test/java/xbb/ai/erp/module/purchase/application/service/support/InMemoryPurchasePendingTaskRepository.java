package xbb.ai.erp.module.purchase.application.service.support;

import xbb.ai.erp.module.purchase.domain.model.PurchasePendingTask;
import xbb.ai.erp.module.purchase.domain.repository.PurchasePendingTaskRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryPurchasePendingTaskRepository implements PurchasePendingTaskRepository {

    private final List<PurchasePendingTask> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1L);

    public List<PurchasePendingTask> all() {
        return data;
    }

    @Override
    public void insert(PurchasePendingTask purchasePendingTask) {
        if (purchasePendingTask.getId() == null) {
            purchasePendingTask.setId(sequence.getAndIncrement());
        }
        data.add(purchasePendingTask);
    }

    @Override
    public void insertBatch(List<PurchasePendingTask> purchasePendingTaskList) {
        purchasePendingTaskList.forEach(this::insert);
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
    public void update(PurchasePendingTask purchasePendingTask) {
        removeById(purchasePendingTask.getCorpid(), purchasePendingTask.getId());
        data.add(purchasePendingTask);
    }

    @Override
    public PurchasePendingTask findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<PurchasePendingTask> findByCondition(Map<String, Object> conditionMap) {
        return data;
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return (long) data.size();
    }
}
