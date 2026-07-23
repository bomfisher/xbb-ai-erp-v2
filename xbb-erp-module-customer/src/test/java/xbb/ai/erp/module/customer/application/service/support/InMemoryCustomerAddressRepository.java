package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.repository.CustomerAddressRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCustomerAddressRepository implements CustomerAddressRepository {

    private final List<CustomerAddress> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<CustomerAddress> all() {
        return data;
    }

    public void seed(CustomerAddress address) {
        data.add(address);
        sequence.updateAndGet(current -> Math.max(current, address.getId() == null ? current : address.getId() + 1));
    }

    @Override
    public void insert(CustomerAddress customerAddress) {
        if (customerAddress.getId() == null) {
            customerAddress.setId(sequence.getAndIncrement());
        }
        data.add(customerAddress);
    }

    @Override
    public void insertBatch(List<CustomerAddress> customerAddresses) {
        customerAddresses.forEach(this::insert);
    }

    @Override
    public void removeById(String corpid, Long id) {
        data.removeIf(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId()));
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        data.removeIf(item -> corpid.equals(item.getCorpid()) && ids.contains(item.getId()));
    }

    @Override
    public void update(CustomerAddress customerAddress) {
        removeById(customerAddress.getCorpid(), customerAddress.getId());
        data.add(customerAddress);
    }

    @Override
    public CustomerAddress findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<CustomerAddress> findByCondition(Map<String, Object> conditionMap) {
        Object corpid = conditionMap.get("corpid");
        Object customerId = conditionMap.get("customerId");
        return data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (customerId == null || customerId.equals(item.getCustomerId()))
        ).toList();
    }
}
