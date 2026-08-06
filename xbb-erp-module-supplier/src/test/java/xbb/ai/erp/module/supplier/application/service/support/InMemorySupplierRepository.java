package xbb.ai.erp.module.supplier.application.service.support;

import xbb.ai.erp.module.supplier.domain.model.Supplier;
import xbb.ai.erp.module.supplier.domain.repository.SupplierRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemorySupplierRepository implements SupplierRepository {

    private final List<Supplier> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<Supplier> all() {
        return data;
    }

    @Override
    public void insert(Supplier supplier) {
        if (supplier.getId() == null) {
            supplier.setId(sequence.getAndIncrement());
        }
        data.add(supplier);
    }

    @Override
    public void insertBatch(List<Supplier> supplierList) {
        supplierList.forEach(this::insert);
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
    public void update(Supplier supplier) {
        removeById(supplier.getCorpid(), supplier.getId());
        data.add(supplier);
    }

    @Override
    public Supplier findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Supplier> findByCondition(Map<String, Object> conditionMap) {
        String corpid = conditionMap == null ? null : (String) conditionMap.get("corpid");
        return data.stream().filter(item -> corpid == null || corpid.equals(item.getCorpid())).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return (long) findByCondition(conditionMap).size();
    }
}
