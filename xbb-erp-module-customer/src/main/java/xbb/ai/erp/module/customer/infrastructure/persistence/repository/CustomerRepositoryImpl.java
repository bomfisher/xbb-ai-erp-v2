package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.common.support.QueryConditionMapHelper;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;
import xbb.ai.erp.module.customer.infrastructure.persistence.convertor.CustomerConvertor;
import xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerMapper;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class CustomerRepositoryImpl implements CustomerRepository {

    private final CustomerMapper customerMapper;

    @Override
    public void insert(Customer customer) {
        CustomerPO po = CustomerConvertor.toPO(customer);
        customerMapper.insert(po);
        customer.setId(po.getId());
    }

    @Override
    public void insertBatch(List<Customer> customers) {
        customerMapper.insertBatch(customers.stream().map(CustomerConvertor::toPO).toList());
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
        customerMapper.update(po);
    }

    @Override
    public Customer findById(String corpid, Long id) {
        return CustomerConvertor.toDomain(customerMapper.findById(corpid, id));
    }

    @Override
    public boolean existsByCustomerCode(String corpid, String customerCode, Long excludeId) {
        CustomerQueryPojo queryPojo = new CustomerQueryPojo();
        queryPojo.setCorpid(corpid);
        queryPojo.setCustomerCode(customerCode);
        queryPojo.setExcludeId(excludeId);
        return customerMapper.findByCondition(QueryConditionMapHelper.prepare(toConditionMap(queryPojo))).stream().anyMatch(po -> po != null && po.getId() != null);
    }

    @Override
    public List<Customer> findByCondition(CustomerQueryPojo queryPojo) {
        Map<String, Object> preparedConditionMap = QueryConditionMapHelper.prepare(toConditionMap(queryPojo));
        return customerMapper.findByCondition(preparedConditionMap)
            .stream()
            .map(CustomerConvertor::toDomain)
            .toList();
    }

    private Map<String, Object> toConditionMap(CustomerQueryPojo queryPojo) {
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        if (queryPojo == null) {
            return conditionMap;
        }
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", queryPojo.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "id", queryPojo.getId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "keyword", queryPojo.getKeyword());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "customerCode", queryPojo.getCustomerCode());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "customerName", queryPojo.getCustomerName());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "customerCategory", queryPojo.getCustomerCategory());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "bizStatus", queryPojo.getBizStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "refStatus", queryPojo.getRefStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "ownerSalesId", queryPojo.getOwnerSalesId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "excludeId", queryPojo.getExcludeId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageNum", queryPojo.getPageNum());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageSize", queryPojo.getPageSize());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "offset", queryPojo.getOffset());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "groupByStr", queryPojo.getGroupByStr());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "orderByStr", queryPojo.getOrderByStr());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "conditions", queryPojo.getConditions());
        return conditionMap;
    }
}
