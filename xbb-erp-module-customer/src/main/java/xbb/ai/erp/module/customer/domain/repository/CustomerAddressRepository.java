package xbb.ai.erp.module.customer.domain.repository;

import xbb.ai.erp.module.customer.domain.model.CustomerAddress;

import java.util.List;
import java.util.Map;

public interface CustomerAddressRepository {
    void insert(CustomerAddress customerAddress);
    void insertBatch(List<CustomerAddress> customerAddresses);
    void removeById(String corpid, Long id);
    void removeBatchByIds(String corpid, List<Long> ids);
    void update(CustomerAddress customerAddress);
    CustomerAddress findById(String corpid, Long id);
    List<CustomerAddress> findByCondition(Map<String, Object> conditionMap);
}
