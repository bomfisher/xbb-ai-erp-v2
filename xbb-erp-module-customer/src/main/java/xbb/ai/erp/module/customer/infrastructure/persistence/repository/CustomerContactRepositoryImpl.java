package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.repository.CustomerContactRepository;
import xbb.ai.erp.module.customer.infrastructure.persistence.convertor.CustomerContactConvertor;
import xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerContactMapper;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerContactPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomerContactRepositoryImpl implements CustomerContactRepository {

    private final CustomerContactMapper customerContactMapper;

    @Override
    public void insert(CustomerContact customerContact) {
        customerContactMapper.insert(CustomerContactConvertor.toPO(customerContact));
    }

    @Override
    public void insertBatch(List<CustomerContact> customerContacts) {
        customerContactMapper.insertBatch(customerContacts.stream().map(CustomerContactConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        customerContactMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        customerContactMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(CustomerContact customerContact) {
        CustomerContactPO po = CustomerContactConvertor.toPO(customerContact);
        customerContactMapper.update(po);
    }

    @Override
    public CustomerContact findById(String corpid, Long id) {
        return CustomerContactConvertor.toDomain(customerContactMapper.findById(corpid, id));
    }

    @Override
    public List<CustomerContact> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return customerContactMapper.findByCondition(preparedConditionMap)
            .stream()
            .map(CustomerContactConvertor::toDomain)
            .toList();
    }
}
