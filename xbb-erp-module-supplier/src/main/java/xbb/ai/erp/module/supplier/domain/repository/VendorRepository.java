package xbb.ai.erp.module.supplier.domain.repository;

import xbb.ai.erp.module.supplier.domain.model.Vendor;

import java.util.List;
import java.util.Map;

public interface VendorRepository {
    void insert(Vendor vendor);

    void insertBatch(List<Vendor> vendorList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(Vendor vendor);

    Vendor findById(String corpid, Long id);

    List<Vendor> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
