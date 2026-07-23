package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;
import xbb.ai.erp.module.customer.domain.repository.CustomerInvoiceProfileRepository;

import java.util.List;
import java.util.Map;

public class FakeCustomerInvoiceProfileRepository implements CustomerInvoiceProfileRepository {

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
        return null;
    }

    @Override
    public List<CustomerInvoiceProfile> findByCondition(Map<String, Object> conditionMap) {
        return List.of();
    }
}
