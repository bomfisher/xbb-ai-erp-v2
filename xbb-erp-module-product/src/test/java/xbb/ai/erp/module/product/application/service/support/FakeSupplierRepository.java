package xbb.ai.erp.module.product.application.service.support;

import xbb.ai.erp.module.supplier.domain.model.Supplier;
import xbb.ai.erp.module.supplier.domain.repository.SupplierRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class FakeSupplierRepository implements SupplierRepository {

    private final List<Supplier> data;

    public FakeSupplierRepository(List<Supplier> data) {
        this.data = new ArrayList<>(data);
    }

    @Override
    public void insert(Supplier supplier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBatch(List<Supplier> supplierList) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeById(String corpid, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(Supplier supplier) {
        throw new UnsupportedOperationException();
    }

    @Override
    public Supplier findById(String corpid, Long id) {
        return data.stream().filter(item -> Objects.equals(corpid, item.getCorpid()) && Objects.equals(id, item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Supplier> findByCondition(Map<String, Object> conditionMap) {
        Object corpid = conditionMap.get("corpid");
        Object id = conditionMap.get("id");
        Object keyword = conditionMap.get("keyword");
        return data.stream().filter(item ->
            (corpid == null || Objects.equals(corpid, item.getCorpid()))
                && (id == null || Objects.equals(id, item.getId()))
                && (keyword == null
                || item.getSupplierCode() != null && item.getSupplierCode().contains(keyword.toString())
                || item.getSupplierName() != null && item.getSupplierName().contains(keyword.toString()))
        ).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        return (long) findByCondition(conditionMap).size();
    }
}
