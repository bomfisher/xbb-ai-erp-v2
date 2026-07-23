package xbb.ai.erp.module.customer.domain.repository;

import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;

import java.util.List;
import java.util.Map;

public interface CustomerBankAccountRepository {
    void insert(CustomerBankAccount bankAccount);

    void insertBatch(List<CustomerBankAccount> bankAccounts);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(CustomerBankAccount bankAccount);

    CustomerBankAccount findById(String corpid, Long id);

    List<CustomerBankAccount> findByCondition(Map<String, Object> conditionMap);
}
