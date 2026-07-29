package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.repository.CustomerAddressRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeCustomerAddressRepository implements CustomerAddressRepository {

    private final List<CustomerAddress> data;
    private int findByConditionCallCount;

    public FakeCustomerAddressRepository(List<CustomerAddress> data) {
        this.data = new ArrayList<>(data);
    }

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
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<CustomerAddress> findByCondition(Map<String, Object> conditionMap) {
        findByConditionCallCount++;
        Object corpid = conditionMap.get("corpid");
        Object customerId = conditionMap.get("customerId");
        Object customerIds = conditionMap.get("customerIds");
        Object defaultFlag = conditionMap.get("defaultFlag");
        return data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (customerId == null || customerId.equals(item.getCustomerId()))
                && (customerIds == null || matchesCustomerIds(customerIds, item.getCustomerId()))
                && (defaultFlag == null || defaultFlag.equals(item.getDefaultFlag()))
        ).toList();
    }

    public int getFindByConditionCallCount() {
        return findByConditionCallCount;
    }

    private boolean matchesCustomerIds(Object customerIds, Long customerId) {
        if (!(customerIds instanceof List<?> idList)) {
            return false;
        }
        return idList.stream().anyMatch(item -> item instanceof Long id && id.equals(customerId));
    }
}
