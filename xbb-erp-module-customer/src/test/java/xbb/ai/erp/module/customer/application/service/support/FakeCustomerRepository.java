package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeCustomerRepository implements CustomerRepository {

    private final List<Customer> data;

    public FakeCustomerRepository(List<Customer> data) {
        this.data = new ArrayList<>(data);
    }

    @Override
    public void insert(Customer customer) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBatch(List<Customer> customers) {
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
    public void update(Customer customer) {
        throw new UnsupportedOperationException();
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
