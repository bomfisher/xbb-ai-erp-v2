package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.common.admin.pojo.ListFilterCondition;
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
        List<ListFilterCondition> conditions = castConditions(conditionMap.get("conditions"));
        return data.stream()
            .filter(item -> corpid == null || corpid.equals(item.getCorpid()))
            .filter(item -> matchConditions(item, conditions))
            .toList();
    }

    @SuppressWarnings("unchecked")
    private static List<ListFilterCondition> castConditions(Object value) {
        if (value instanceof List<?> list) {
            return (List<ListFilterCondition>) list;
        }
        return List.of();
    }

    private static boolean matchConditions(Customer item, List<ListFilterCondition> conditions) {
        for (ListFilterCondition condition : conditions) {
            if (!matchCondition(item, condition)) {
                return false;
            }
        }
        return true;
    }

    private static boolean matchCondition(Customer item, ListFilterCondition condition) {
        if (!"customer_code".equals(condition.getAttr())) {
            throw new IllegalArgumentException("Unsupported filter attr: " + condition.getAttr());
        }
        if (!"EQ".equals(condition.getSymbol())) {
            throw new IllegalArgumentException("Unsupported filter symbol: " + condition.getSymbol());
        }
        List<String> values = condition.getValue();
        if (values == null || values.size() != 1) {
            throw new IllegalArgumentException("Missing customer code filter value");
        }
        return values.get(0).equals(item.getCustomerCode());
    }
}
