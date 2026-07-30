package xbb.ai.erp.module.customer.domain.repository;

import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;
import xbb.ai.erp.module.customer.domain.pojo.CustomerInvoiceProfileQueryPojo;

import java.util.List;

public interface CustomerInvoiceProfileRepository {
    void insert(CustomerInvoiceProfile invoiceProfile);

    void insertBatch(List<CustomerInvoiceProfile> invoiceProfiles);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(CustomerInvoiceProfile invoiceProfile);

    CustomerInvoiceProfile findById(String corpid, Long id);

    List<CustomerInvoiceProfile> findByCondition(CustomerInvoiceProfileQueryPojo queryPojo);
}
