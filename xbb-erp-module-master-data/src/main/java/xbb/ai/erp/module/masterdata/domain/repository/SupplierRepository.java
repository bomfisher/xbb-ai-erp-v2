package xbb.ai.erp.module.masterdata.domain.repository;

import xbb.ai.erp.module.masterdata.domain.model.Supplier;

import java.util.List;
import java.util.Map;

public interface SupplierRepository {
    Long insert(Supplier supplier);

    void insertBatch(List<Supplier> supplierList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(Supplier supplier);

    Supplier findById(String corpid, Long id);

    List<Supplier> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
