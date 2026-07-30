package xbb.ai.erp.module.customer.domain.repository;

import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo;

import java.util.List;

public interface CustomerRepository {
    void insert(Customer customer);
    void insertBatch(List<Customer> customers);
    void removeById(String corpid, Long id);
    void removeBatchByIds(String corpid, List<Long> ids);
    void update(Customer customer);
    Customer findById(String corpid, Long id);
    List<Customer> findByCondition(CustomerQueryPojo queryPojo);
    boolean existsByCustomerCode(String corpid, String customerCode, Long excludeId);
}
