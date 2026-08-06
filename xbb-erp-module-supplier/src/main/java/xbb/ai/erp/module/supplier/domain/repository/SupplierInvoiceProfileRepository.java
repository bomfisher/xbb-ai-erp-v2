package xbb.ai.erp.module.supplier.domain.repository;

import xbb.ai.erp.module.supplier.domain.model.SupplierInvoiceProfile;

import java.util.List;
import java.util.Map;

public interface SupplierInvoiceProfileRepository {
    void insert(SupplierInvoiceProfile supplierInvoiceProfile);

    void insertBatch(List<SupplierInvoiceProfile> supplierInvoiceProfileList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(SupplierInvoiceProfile supplierInvoiceProfile);

    SupplierInvoiceProfile findById(String corpid, Long id);

    List<SupplierInvoiceProfile> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
