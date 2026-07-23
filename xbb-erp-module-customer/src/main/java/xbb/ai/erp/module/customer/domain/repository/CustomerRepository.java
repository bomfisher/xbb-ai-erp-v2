package xbb.ai.erp.module.customer.domain.repository;

import xbb.ai.erp.module.customer.domain.model.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerRepository {
    void insert(Customer customer);
    void insertBatch(List<Customer> customers);
    void removeById(String corpid, Long id);
    void removeBatchByIds(String corpid, List<Long> ids);
    void update(Customer customer);
    Customer findById(String corpid, Long id);
    List<Customer> findByCondition(Map<String, Object> conditionMap);
}
