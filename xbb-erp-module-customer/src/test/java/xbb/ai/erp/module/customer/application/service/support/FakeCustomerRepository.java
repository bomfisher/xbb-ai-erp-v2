package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.Comparator;
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
        String customerCode = asString(conditionMap.get("customerCode"));
        String customerName = asString(conditionMap.get("customerName"));
        String customerCategory = asString(conditionMap.get("customerCategory"));
        String regionCode = asString(conditionMap.get("regionCode"));
        String ownerSalesId = asString(conditionMap.get("ownerSalesId"));
        String bizStatus = asString(conditionMap.get("bizStatus"));
        String refStatus = asString(conditionMap.get("refStatus"));
        String keyword = asString(conditionMap.get("keyword"));
        Integer pageNum = asInteger(conditionMap.get("pageNum"));
        Integer pageSize = asInteger(conditionMap.get("pageSize"));

        List<Customer> filtered = data.stream()
            .filter(item -> corpid == null || corpid.equals(item.getCorpid()))
            .filter(item -> customerCode == null || contains(item.getCustomerCode(), customerCode))
            .filter(item -> customerName == null || contains(item.getCustomerName(), customerName))
            .filter(item -> customerCategory == null || customerCategory.equals(item.getCustomerCategory()))
            .filter(item -> regionCode == null || regionCode.equals(item.getRegionCode()))
            .filter(item -> ownerSalesId == null || ownerSalesId.equals(item.getOwnerSalesId()))
            .filter(item -> bizStatus == null || bizStatus.equals(item.getBizStatus()))
            .filter(item -> refStatus == null || refStatus.equals(item.getRefStatus()))
            .filter(item -> keyword == null || contains(item.getCustomerCode(), keyword) || contains(item.getCustomerName(), keyword))
            .sorted(Comparator.comparing(Customer::getId))
            .toList();

        if (pageNum == null || pageSize == null || pageSize <= 0) {
            return filtered;
        }

        int fromIndex = Math.max((pageNum - 1) * pageSize, 0);
        if (fromIndex >= filtered.size()) {
            return List.of();
        }
        int toIndex = Math.min(fromIndex + pageSize, filtered.size());
        return filtered.subList(fromIndex, toIndex);
    }

    private static String asString(Object value) {
        if (!(value instanceof String text) || text.isBlank()) {
            return null;
        }
        return text;
    }

    private static Integer asInteger(Object value) {
        return value instanceof Integer integer ? integer : null;
    }

    private static boolean contains(String source, String target) {
        return source != null && source.contains(target);
    }
}
