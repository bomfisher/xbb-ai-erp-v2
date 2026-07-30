package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.pojo.CustomerBankAccountQueryPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerBankAccountRepository;

import java.util.List;

public class FakeCustomerBankAccountRepository implements CustomerBankAccountRepository {

    @Override
    public void insert(CustomerBankAccount bankAccount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBatch(List<CustomerBankAccount> bankAccounts) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeById(String corpid, Long id) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void update(CustomerBankAccount bankAccount) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CustomerBankAccount findById(String corpid, Long id) {
        return null;
    }

    @Override
    public List<CustomerBankAccount> findByCondition(CustomerBankAccountQueryPojo queryPojo) {
        return List.of();
    }
}
