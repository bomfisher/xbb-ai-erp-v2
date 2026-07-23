package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;
import xbb.ai.erp.module.customer.domain.repository.CustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.infrastructure.persistence.convertor.CustomerInvoiceProfileConvertor;
import xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerInvoiceProfileMapper;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerInvoiceProfilePO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomerInvoiceProfileRepositoryImpl implements CustomerInvoiceProfileRepository {

    private final CustomerInvoiceProfileMapper customerInvoiceProfileMapper;

    @Override
    public void insert(CustomerInvoiceProfile invoiceProfile) {
        customerInvoiceProfileMapper.insert(CustomerInvoiceProfileConvertor.toPO(invoiceProfile));
    }

    @Override
    public void insertBatch(List<CustomerInvoiceProfile> invoiceProfiles) {
        customerInvoiceProfileMapper.insertBatch(invoiceProfiles.stream().map(CustomerInvoiceProfileConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        customerInvoiceProfileMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        customerInvoiceProfileMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(CustomerInvoiceProfile invoiceProfile) {
        CustomerInvoiceProfilePO po = CustomerInvoiceProfileConvertor.toPO(invoiceProfile);
        customerInvoiceProfileMapper.update(po);
    }

    @Override
    public CustomerInvoiceProfile findById(String corpid, Long id) {
        return CustomerInvoiceProfileConvertor.toDomain(customerInvoiceProfileMapper.findById(corpid, id));
    }

    @Override
    public List<CustomerInvoiceProfile> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return customerInvoiceProfileMapper.findByCondition(preparedConditionMap)
            .stream()
            .map(CustomerInvoiceProfileConvertor::toDomain)
            .toList();
    }
}
