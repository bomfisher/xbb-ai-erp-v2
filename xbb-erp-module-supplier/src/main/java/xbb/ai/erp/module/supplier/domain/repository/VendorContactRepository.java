package xbb.ai.erp.module.supplier.domain.repository;

import xbb.ai.erp.module.supplier.domain.model.VendorContact;

import java.util.List;
import java.util.Map;

public interface VendorContactRepository {
    void insert(VendorContact vendorContact);

    void insertBatch(List<VendorContact> vendorContactList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(VendorContact vendorContact);

    VendorContact findById(String corpid, Long id);

    List<VendorContact> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
