package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.repository.CustomerContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCustomerContactRepository implements CustomerContactRepository {

    private final List<CustomerContact> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<CustomerContact> all() {
        return data;
    }

    public void seed(CustomerContact contact) {
        data.add(contact);
        sequence.updateAndGet(current -> Math.max(current, contact.getId() == null ? current : contact.getId() + 1));
    }

    @Override
    public void insert(CustomerContact customerContact) {
        if (customerContact.getId() == null) {
            customerContact.setId(sequence.getAndIncrement());
        }
        data.add(customerContact);
    }

    @Override
    public void insertBatch(List<CustomerContact> customerContacts) {
        customerContacts.forEach(this::insert);
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
    public void update(CustomerContact customerContact) {
        removeById(customerContact.getCorpid(), customerContact.getId());
        data.add(customerContact);
    }

    @Override
    public CustomerContact findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<CustomerContact> findByCondition(Map<String, Object> conditionMap) {
        Object corpid = conditionMap.get("corpid");
        Object customerId = conditionMap.get("customerId");
        return data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (customerId == null || customerId.equals(item.getCustomerId()))
        ).toList();
    }
}
