package xbb.ai.erp.module.supplier.domain.repository;

import xbb.ai.erp.module.supplier.domain.model.SupplierBankAccount;

import java.util.List;
import java.util.Map;

public interface SupplierBankAccountRepository {
    void insert(SupplierBankAccount supplierBankAccount);

    void insertBatch(List<SupplierBankAccount> supplierBankAccountList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(SupplierBankAccount supplierBankAccount);

    SupplierBankAccount findById(String corpid, Long id);

    List<SupplierBankAccount> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
