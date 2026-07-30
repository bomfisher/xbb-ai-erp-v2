package xbb.ai.erp.module.customer.domain.repository;

import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.pojo.CustomerAddressQueryPojo;

import java.util.List;

public interface CustomerAddressRepository {
    void insert(CustomerAddress customerAddress);
    void insertBatch(List<CustomerAddress> customerAddresses);
    void removeById(String corpid, Long id);
    void removeBatchByIds(String corpid, List<Long> ids);
    void update(CustomerAddress customerAddress);
    CustomerAddress findById(String corpid, Long id);
    List<CustomerAddress> findByCondition(CustomerAddressQueryPojo queryPojo);
}
