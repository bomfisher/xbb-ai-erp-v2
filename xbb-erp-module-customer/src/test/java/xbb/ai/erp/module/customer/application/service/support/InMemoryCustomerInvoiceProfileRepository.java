package xbb.ai.erp.module.customer.application.service.support;

import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;
import xbb.ai.erp.module.customer.domain.pojo.CustomerInvoiceProfileQueryPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerInvoiceProfileRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

public class InMemoryCustomerInvoiceProfileRepository implements CustomerInvoiceProfileRepository {

    private final List<CustomerInvoiceProfile> data = new ArrayList<>();
    private final AtomicLong sequence = new AtomicLong(1);

    public List<CustomerInvoiceProfile> all() {
        return data;
    }

    public void seed(CustomerInvoiceProfile invoiceProfile) {
        data.add(invoiceProfile);
        sequence.updateAndGet(current -> Math.max(current, invoiceProfile.getId() == null ? current : invoiceProfile.getId() + 1));
    }

    @Override
    public void insert(CustomerInvoiceProfile invoiceProfile) {
        if (invoiceProfile.getId() == null) {
            invoiceProfile.setId(sequence.getAndIncrement());
        }
        data.add(invoiceProfile);
    }

    @Override
    public void insertBatch(List<CustomerInvoiceProfile> invoiceProfiles) {
        invoiceProfiles.forEach(this::insert);
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
    public void update(CustomerInvoiceProfile invoiceProfile) {
        removeById(invoiceProfile.getCorpid(), invoiceProfile.getId());
        data.add(invoiceProfile);
    }

    @Override
    public CustomerInvoiceProfile findById(String corpid, Long id) {
        return data.stream().filter(item -> corpid.equals(item.getCorpid()) && id.equals(item.getId())).findFirst().orElse(null);
    }

    @Override
    public List<CustomerInvoiceProfile> findByCondition(CustomerInvoiceProfileQueryPojo queryPojo) {
        String corpid = queryPojo == null ? null : queryPojo.getCorpid();
        Long customerId = queryPojo == null ? null : queryPojo.getCustomerId();
        return data.stream().filter(item ->
            (corpid == null || corpid.equals(item.getCorpid()))
                && (customerId == null || customerId.equals(item.getCustomerId()))
        ).toList();
    }
}
