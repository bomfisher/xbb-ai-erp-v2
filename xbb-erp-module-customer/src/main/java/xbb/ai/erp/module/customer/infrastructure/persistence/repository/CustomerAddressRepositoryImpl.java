package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.common.support.QueryConditionMapHelper;
import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.pojo.CustomerAddressQueryPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerAddressRepository;
import xbb.ai.erp.module.customer.infrastructure.persistence.convertor.CustomerAddressConvertor;
import xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerAddressMapper;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerAddressPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomerAddressRepositoryImpl implements CustomerAddressRepository {

    private final CustomerAddressMapper customerAddressMapper;

    @Override
    public void insert(CustomerAddress customerAddress) {
        customerAddressMapper.insert(CustomerAddressConvertor.toPO(customerAddress));
    }

    @Override
    public void insertBatch(List<CustomerAddress> customerAddresses) {
        customerAddressMapper.insertBatch(customerAddresses.stream().map(CustomerAddressConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        customerAddressMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        customerAddressMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(CustomerAddress customerAddress) {
        CustomerAddressPO po = CustomerAddressConvertor.toPO(customerAddress);
        customerAddressMapper.update(po);
    }

    @Override
    public CustomerAddress findById(String corpid, Long id) {
        return CustomerAddressConvertor.toDomain(customerAddressMapper.findById(corpid, id));
    }

    @Override
    public List<CustomerAddress> findByCondition(CustomerAddressQueryPojo queryPojo) {
        Map<String, Object> preparedConditionMap = QueryConditionMapHelper.prepare(toConditionMap(queryPojo));
        return customerAddressMapper.findByCondition(preparedConditionMap)
            .stream()
            .map(CustomerAddressConvertor::toDomain)
            .toList();
    }

    private Map<String, Object> toConditionMap(CustomerAddressQueryPojo queryPojo) {
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        if (queryPojo == null) {
            return conditionMap;
        }
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", queryPojo.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "id", queryPojo.getId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "customerId", queryPojo.getCustomerId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "customerIds", queryPojo.getCustomerIds());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "addressType", queryPojo.getAddressType());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "receiverName", queryPojo.getReceiverName());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "receiverMobile", queryPojo.getReceiverMobile());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "bizStatus", queryPojo.getBizStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "defaultFlag", queryPojo.getDefaultFlag());
        return conditionMap;
    }
}
