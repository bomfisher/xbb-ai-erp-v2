package xbb.ai.erp.module.masterdata.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.masterdata.domain.model.Customer;
import xbb.ai.erp.module.masterdata.domain.repository.CustomerRepository;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.convertor.CustomerConvertor;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper.CustomerMapper;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.CustomerPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleMasterdataCustomerRepositoryImpl")
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerMapper customerMapper;

    @Override
    public Long insert(Customer customer) {
        CustomerPO po = CustomerConvertor.toPO(customer);
        initializeForInsert(po);
        po.setId(null);
        customerMapper.insert(po);
        customer.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<Customer> customerList) {
        List<CustomerPO> poList = customerList.stream().map(CustomerConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        customerMapper.insertBatch(poList);
        for (int index = 0; index < customerList.size(); index++) {
            customerList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        customerMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        customerMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(Customer customer) {
        CustomerPO po = CustomerConvertor.toPO(customer);
        po.setUpdateTime(System.currentTimeMillis());
        customerMapper.update(po);
    }

    @Override
    public void updateDefaultContactId(String corpid, Long id, Long defaultContactId) {
        customerMapper.updateDefaultContactId(corpid, id, defaultContactId, System.currentTimeMillis());
    }

    @Override
    public Customer findById(String corpid, Long id) {
        return CustomerConvertor.toDomain(customerMapper.findById(corpid, id));
    }

    @Override
    public List<Customer> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return customerMapper.findByCondition(preparedConditionMap).stream().map(CustomerConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return customerMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
