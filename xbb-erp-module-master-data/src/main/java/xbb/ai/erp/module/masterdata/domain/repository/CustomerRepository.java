package xbb.ai.erp.module.masterdata.domain.repository;

import xbb.ai.erp.module.masterdata.domain.model.Customer;

import java.util.List;
import java.util.Map;

public interface CustomerRepository {
    Long insert(Customer customer);

    void insertBatch(List<Customer> customerList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(Customer customer);

    void updateDefaultContactId(String corpid, Long id, Long defaultContactId);

    Customer findById(String corpid, Long id);

    List<Customer> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
