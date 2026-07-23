package xbb.ai.erp.module.supplier.domain.repository;

import xbb.ai.erp.module.supplier.domain.model.VendorInvoiceProfile;

import java.util.List;
import java.util.Map;

public interface VendorInvoiceProfileRepository {
    void insert(VendorInvoiceProfile vendorInvoiceProfile);

    void insertBatch(List<VendorInvoiceProfile> vendorInvoiceProfileList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(VendorInvoiceProfile vendorInvoiceProfile);

    VendorInvoiceProfile findById(String corpid, Long id);

    List<VendorInvoiceProfile> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
