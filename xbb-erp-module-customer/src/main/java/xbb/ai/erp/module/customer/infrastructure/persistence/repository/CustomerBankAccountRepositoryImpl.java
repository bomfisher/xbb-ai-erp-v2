package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.repository.CustomerBankAccountRepository;
import xbb.ai.erp.module.customer.infrastructure.persistence.convertor.CustomerBankAccountConvertor;
import xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerBankAccountMapper;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerBankAccountPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomerBankAccountRepositoryImpl implements CustomerBankAccountRepository {

    private final CustomerBankAccountMapper customerBankAccountMapper;

    @Override
    public void insert(CustomerBankAccount bankAccount) {
        customerBankAccountMapper.insert(CustomerBankAccountConvertor.toPO(bankAccount));
    }

    @Override
    public void insertBatch(List<CustomerBankAccount> bankAccounts) {
        customerBankAccountMapper.insertBatch(bankAccounts.stream().map(CustomerBankAccountConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        customerBankAccountMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        customerBankAccountMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(CustomerBankAccount bankAccount) {
        CustomerBankAccountPO po = CustomerBankAccountConvertor.toPO(bankAccount);
        customerBankAccountMapper.update(po);
    }

    @Override
    public CustomerBankAccount findById(String corpid, Long id) {
        return CustomerBankAccountConvertor.toDomain(customerBankAccountMapper.findById(corpid, id));
    }

    @Override
    public List<CustomerBankAccount> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return customerBankAccountMapper.findByCondition(preparedConditionMap)
            .stream()
            .map(CustomerBankAccountConvertor::toDomain)
            .toList();
    }
}
