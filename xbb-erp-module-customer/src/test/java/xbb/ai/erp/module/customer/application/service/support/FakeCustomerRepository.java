package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.common.admin.pojo.ListFilterCondition;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    public boolean existsByCustomerCode(String corpid, String customerCode, Long excludeId) {
        return data.stream()
            .filter(item -> corpid.equals(item.getCorpid()))
            .filter(item -> customerCode.equals(item.getCustomerCode()))
            .anyMatch(item -> excludeId == null || !excludeId.equals(item.getId()));
    }

    @Override
    public List<Customer> findByCondition(CustomerQueryPojo queryPojo) {
        String corpid = queryPojo == null ? null : queryPojo.getCorpid();
        String keyword = queryPojo == null ? null : queryPojo.getKeyword();
        Integer pageNum = queryPojo == null ? null : queryPojo.getPageNum();
        Integer pageSize = queryPojo == null ? null : queryPojo.getPageSize();
        List<ListFilterCondition> conditions = castConditions(queryPojo == null ? null : queryPojo.getConditions());

        List<Customer> filtered = data.stream()
            .filter(item -> corpid == null || corpid.equals(item.getCorpid()))
            .filter(item -> keyword == null || contains(item.getCustomerCode(), keyword) || contains(item.getCustomerName(), keyword))
            .filter(item -> matchConditions(item, conditions))
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

    @SuppressWarnings("unchecked")
    private static List<ListFilterCondition> castConditions(Object value) {
        if (value == null) {
            return List.of();
        }
        if (!(value instanceof List<?> list)) {
            throw new BizException("筛选条件格式不合法");
        }
        for (Object item : list) {
            if (!(item instanceof ListFilterCondition)) {
                throw new BizException("筛选条件格式不合法");
            }
        }
        return (List<ListFilterCondition>) list;
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
        String value = condition.getValue() == null || condition.getValue().isEmpty() ? null : condition.getValue().get(0);
        return switch (condition.getAttr()) {
            case "customer_code" -> matchText(item.getCustomerCode(), condition.getSymbol(), value, condition.getValue());
            case "customer_name" -> matchText(item.getCustomerName(), condition.getSymbol(), value, condition.getValue());
            case "customer_category" -> matchText(item.getCustomerCategory(), condition.getSymbol(), value, condition.getValue());
            case "owner_sales_id" -> matchText(item.getOwnerSalesId(), condition.getSymbol(), value, condition.getValue());
            case "biz_status" -> matchText(item.getBizStatus(), condition.getSymbol(), value, condition.getValue());
            case "region_code" -> matchText(item.getRegionCode(), condition.getSymbol(), value, condition.getValue());
            case "add_time" -> matchNumber(item.getAddTime(), condition.getSymbol(), condition.getValue());
            default -> throw new BizException("筛选字段不合法: " + condition.getAttr());
        };
    }

    private static boolean matchText(String source, String symbol, String value, List<String> values) {
        return switch (symbol) {
            case "EQ" -> source != null && source.equals(value);
            case "NE" -> source == null || !source.equals(value);
            case "CONTAINS" -> contains(source, value);
            case "NOT_CONTAINS" -> source == null || !source.contains(value);
            case "IN" -> source != null && values != null && values.contains(source);
            case "IS_EMPTY" -> source == null || source.isBlank();
            case "IS_NOT_EMPTY" -> source != null && !source.isBlank();
            default -> throw new BizException("筛选操作符不支持: " + symbol);
        };
    }

    private static boolean matchNumber(Long source, String symbol, List<String> values) {
        return switch (symbol) {
            case "EQ" -> source != null && source.equals(parseLong(values, 0));
            case "GE" -> source != null && source >= parseLong(values, 0);
            case "LE" -> source != null && source <= parseLong(values, 0);
            case "BETWEEN" -> source != null && source >= parseLong(values, 0) && source <= parseLong(values, 1);
            case "IS_EMPTY" -> source == null;
            case "IS_NOT_EMPTY" -> source != null;
            default -> throw new BizException("筛选操作符不支持: " + symbol);
        };
    }

    private static Long parseLong(List<String> values, int index) {
        if (values == null || values.size() <= index) {
            throw new IllegalArgumentException("Missing numeric filter value");
        }
        return Long.parseLong(values.get(index));
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
