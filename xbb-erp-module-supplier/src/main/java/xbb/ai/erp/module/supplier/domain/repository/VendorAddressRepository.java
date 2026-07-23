package xbb.ai.erp.module.supplier.domain.repository;

import xbb.ai.erp.module.supplier.domain.model.VendorAddress;

import java.util.List;
import java.util.Map;

public interface VendorAddressRepository {
    void insert(VendorAddress vendorAddress);

    void insertBatch(List<VendorAddress> vendorAddressList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(VendorAddress vendorAddress);

    VendorAddress findById(String corpid, Long id);

    List<VendorAddress> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
