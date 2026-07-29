package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;
import xbb.ai.erp.module.customer.domain.repository.CustomerInvoiceProfileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeCustomerInvoiceProfileRepository implements CustomerInvoiceProfileRepository {

    private final List<CustomerInvoiceProfile> data;
    private int findByConditionCallCount;

    public FakeCustomerInvoiceProfileRepository(List<CustomerInvoiceProfile> data) {
        this.data = new ArrayList<>(data);
    }

    @Override
    public void insert(CustomerInvoiceProfile invoiceProfile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBatch(List<CustomerInvoiceProfile> invoiceProfiles) {
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
    public void update(CustomerInvoiceProfile invoiceProfile) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CustomerInvoiceProfile findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<CustomerInvoiceProfile> findByCondition(Map<String, Object> conditionMap) {
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
