package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.repository.CustomerContactRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FakeCustomerContactRepository implements CustomerContactRepository {

    private final List<CustomerContact> data;

    public FakeCustomerContactRepository(List<CustomerContact> data) {
        this.data = new ArrayList<>(data);
    }

    @Override
    public void insert(CustomerContact customerContact) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void insertBatch(List<CustomerContact> customerContacts) {
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
    public void update(CustomerContact customerContact) {
        throw new UnsupportedOperationException();
    }

    @Override
    public CustomerContact findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<CustomerContact> findByCondition(Map<String, Object> conditionMap) {
        Object corpid = conditionMap.get("corpid");
        Object customerId = conditionMap.get("customerId");
        Object defaultFlag = conditionMap.get("defaultFlag");
        return data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (customerId == null || customerId.equals(item.getCustomerId()))
                && (defaultFlag == null || defaultFlag.equals(item.getDefaultFlag()))
        ).toList();
    }
}
