package xbb.ai.erp.module.supplier.domain.repository;

import xbb.ai.erp.module.supplier.domain.model.VendorBankAccount;

import java.util.List;
import java.util.Map;

public interface VendorBankAccountRepository {
    void insert(VendorBankAccount vendorBankAccount);

    void insertBatch(List<VendorBankAccount> vendorBankAccountList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(VendorBankAccount vendorBankAccount);

    VendorBankAccount findById(String corpid, Long id);

    List<VendorBankAccount> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
