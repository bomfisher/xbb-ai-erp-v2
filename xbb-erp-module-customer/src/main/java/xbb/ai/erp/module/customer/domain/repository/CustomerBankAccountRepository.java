package xbb.ai.erp.module.customer.domain.repository;

import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.pojo.CustomerBankAccountQueryPojo;

import java.util.List;

public interface CustomerBankAccountRepository {
    void insert(CustomerBankAccount bankAccount);

    void insertBatch(List<CustomerBankAccount> bankAccounts);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(CustomerBankAccount bankAccount);

    CustomerBankAccount findById(String corpid, Long id);

    List<CustomerBankAccount> findByCondition(CustomerBankAccountQueryPojo queryPojo);
}
