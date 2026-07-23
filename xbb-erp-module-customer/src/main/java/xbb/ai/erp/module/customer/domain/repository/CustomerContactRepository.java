package xbb.ai.erp.module.customer.domain.repository;

import xbb.ai.erp.module.customer.domain.model.CustomerContact;

import java.util.List;
import java.util.Map;

public interface CustomerContactRepository {
    void insert(CustomerContact customerContact);
    void insertBatch(List<CustomerContact> customerContacts);
    void removeById(String corpid, Long id);
    void removeBatchByIds(String corpid, List<Long> ids);
    void update(CustomerContact customerContact);
    CustomerContact findById(String corpid, Long id);
    List<CustomerContact> findByCondition(Map<String, Object> conditionMap);
}
