package xbb.ai.erp.module.supplier.application.service.support;

import xbb.ai.erp.module.supplier.domain.model.SupplierAddress;
import xbb.ai.erp.module.supplier.domain.repository.SupplierAddressRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemorySupplierAddressRepository implements SupplierAddressRepository {

    private final List<SupplierAddress> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<SupplierAddress> all() {
        return data;
    }

    @Override
    public void insert(SupplierAddress supplierAddress) {
        if (supplierAddress.getId() == null) {
            supplierAddress.setId(sequence.getAndIncrement());
        }
        data.add(supplierAddress);
    }

    @Override
    public void insertBatch(List<SupplierAddress> supplierAddressList) {
        supplierAddressList.forEach(this::insert);
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
    public void update(SupplierAddress supplierAddress) {
        removeById(supplierAddress.getCorpid(), supplierAddress.getId());
        data.add(supplierAddress);
    }

    @Override
    public SupplierAddress findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<SupplierAddress> findByCondition(Map<String, Object> conditionMap) {
        String corpid = conditionMap == null ? null : (String) conditionMap.get("corpid");
        Long supplierId = conditionMap == null ? null : (Long) conditionMap.get("supplierId");
        return data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (supplierId == null || supplierId.equals(item.getSupplierId()))
        ).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return (long) findByCondition(conditionMap).size();
    }
}
