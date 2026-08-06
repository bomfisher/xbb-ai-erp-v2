package xbb.ai.erp.module.supplier.domain.repository;

import xbb.ai.erp.module.supplier.domain.model.SupplierAddress;

import java.util.List;
import java.util.Map;

public interface SupplierAddressRepository {
    void insert(SupplierAddress supplierAddress);

    void insertBatch(List<SupplierAddress> supplierAddressList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(SupplierAddress supplierAddress);

    SupplierAddress findById(String corpid, Long id);

    List<SupplierAddress> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
