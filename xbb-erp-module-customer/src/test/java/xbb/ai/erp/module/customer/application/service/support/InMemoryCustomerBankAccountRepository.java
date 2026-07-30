package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.pojo.CustomerBankAccountQueryPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerBankAccountRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCustomerBankAccountRepository implements CustomerBankAccountRepository {

    private final List<CustomerBankAccount> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<CustomerBankAccount> all() {
        return data;
    }

    public void seed(CustomerBankAccount bankAccount) {
        data.add(bankAccount);
        sequence.updateAndGet(current -> Math.max(current, bankAccount.getId() == null ? current : bankAccount.getId() + 1));
    }

    @Override
    public void insert(CustomerBankAccount bankAccount) {
        if (bankAccount.getId() == null) {
            bankAccount.setId(sequence.getAndIncrement());
        }
        data.add(bankAccount);
    }

    @Override
    public void insertBatch(List<CustomerBankAccount> bankAccounts) {
        bankAccounts.forEach(this::insert);
    }

    @Override
    public void removeById(String corpid, Long id) {
        data.removeIf(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId()));
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        data.removeIf(item -> corpid.equals(item.getCorpid()) && ids.contains(item.getId()));
    }

    @Override
    public void update(CustomerBankAccount bankAccount) {
        removeById(bankAccount.getCorpid(), bankAccount.getId());
        data.add(bankAccount);
    }

    @Override
    public CustomerBankAccount findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<CustomerBankAccount> findByCondition(CustomerBankAccountQueryPojo queryPojo) {
        String corpid = queryPojo == null ? null : queryPojo.getCorpid();
        Long customerId = queryPojo == null ? null : queryPojo.getCustomerId();
        return data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (customerId == null || customerId.equals(item.getCustomerId()))
        ).toList();
    }
}
