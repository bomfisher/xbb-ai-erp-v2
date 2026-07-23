package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;
import xbb.ai.erp.module.customer.infrastructure.persistence.convertor.CustomerConvertor;
import xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerMapper;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerMapper customerMapper;

    @Override
    public void insert(Customer customer) {
        customerMapper.insert(CustomerConvertor.toPO(customer));
    }

    @Override
    public void insertBatch(List<Customer> customers) {
        customerMapper.insertBatch(customers.stream().map(CustomerConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        customerMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        customerMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(Customer customer) {
        CustomerPO po = CustomerConvertor.toPO(customer);
        customerMapper.update(po);
    }

    @Override
    public Customer findById(String corpid, Long id) {
        return CustomerConvertor.toDomain(customerMapper.findById(corpid, id));
    }

    @Override
    public List<Customer> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return customerMapper.findByCondition(preparedConditionMap)
            .stream()
            .map(CustomerConvertor::toDomain)
            .toList();
    }
}
