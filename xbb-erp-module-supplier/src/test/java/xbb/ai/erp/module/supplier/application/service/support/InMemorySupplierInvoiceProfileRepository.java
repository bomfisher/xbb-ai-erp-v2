package xbb.ai.erp.module.supplier.application.service.support;

import xbb.ai.erp.module.supplier.domain.model.SupplierInvoiceProfile;
import xbb.ai.erp.module.supplier.domain.repository.SupplierInvoiceProfileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemorySupplierInvoiceProfileRepository implements SupplierInvoiceProfileRepository {

    private final List<SupplierInvoiceProfile> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<SupplierInvoiceProfile> all() {
        return data;
    }

    @Override
    public void insert(SupplierInvoiceProfile supplierInvoiceProfile) {
        if (supplierInvoiceProfile.getId() == null) {
            supplierInvoiceProfile.setId(sequence.getAndIncrement());
        }
        data.add(supplierInvoiceProfile);
    }

    @Override
    public void insertBatch(List<SupplierInvoiceProfile> supplierInvoiceProfileList) {
        supplierInvoiceProfileList.forEach(this::insert);
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
    public void update(SupplierInvoiceProfile supplierInvoiceProfile) {
        removeById(supplierInvoiceProfile.getCorpid(), supplierInvoiceProfile.getId());
        data.add(supplierInvoiceProfile);
    }

    @Override
    public SupplierInvoiceProfile findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<SupplierInvoiceProfile> findByCondition(Map<String, Object> conditionMap) {
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
