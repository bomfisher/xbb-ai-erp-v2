package xbb.ai.erp.module.supplier.domain.repository;

import xbb.ai.erp.module.supplier.domain.model.SupplierContact;

import java.util.List;
import java.util.Map;

public interface SupplierContactRepository {
    void insert(SupplierContact supplierContact);

    void insertBatch(List<SupplierContact> supplierContactList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(SupplierContact supplierContact);

    SupplierContact findById(String corpid, Long id);

    List<SupplierContact> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
