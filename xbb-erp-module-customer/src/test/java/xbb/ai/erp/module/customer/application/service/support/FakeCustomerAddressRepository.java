package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.repository.CustomerAddressRepository;

import java.util.List;
import java.util.Map;

public class FakeCustomerAddressRepository implements CustomerAddressRepository {

    @Override
    public void insert(CustomerAddress customerAddress) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBatch(List<CustomerAddress> customerAddresses) {
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
    public void update(CustomerAddress customerAddress) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CustomerAddress findById(String corpid, Long id) {
        return null;
    }

    @Override
    public List<CustomerAddress> findByCondition(Map<String, Object> conditionMap) {
        return List.of();
    }
}
