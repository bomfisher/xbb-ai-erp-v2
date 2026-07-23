package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCustomerRepository implements CustomerRepository {

    private final List<Customer> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<Customer> all() {
        return data;
    }

    public void seed(Customer customer) {
        data.add(customer);
        sequence.updateAndGet(current -> Math.max(current, customer.getId() == null ? current : customer.getId() + 1));
    }

    @Override
    public void insert(Customer customer) {
        if (customer.getId() == null) {
            customer.setId(sequence.getAndIncrement());
        }
        data.add(customer);
    }

    @Override
    public void insertBatch(List<Customer> customers) {
        customers.forEach(this::insert);
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
    public void update(Customer customer) {
        removeById(customer.getCorpid(), customer.getId());
        data.add(customer);
    }

    @Override
    public Customer findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<Customer> findByCondition(Map<String, Object> conditionMap) {
        Object corpid = conditionMap.get("corpid");
        return data.stream().filter(item -> corpid == null || corpid.equals(item.getCorpid())).toList();
    }
}
