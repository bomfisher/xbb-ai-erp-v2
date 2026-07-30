package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo;
import xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerMapper;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyMap;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CustomerRepositoryImplTest {

    @Test
    void should_write_back_generated_id_after_insert() {
        CustomerMapper customerMapper = mock(CustomerMapper.class);
        doAnswer(invocation -> {
            CustomerPO po = invocation.getArgument(0);
            po.setId(101L);
            return 1;
        }).when(customerMapper).insert(any(CustomerPO.class));

        CustomerRepositoryImpl repository = new CustomerRepositoryImpl(customerMapper);
        Customer customer = new Customer();
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customer.setCustomerName("杭州客户");
        customer.setCustomerCategory("A");
        customer.setBizStatus("1");
        customer.setRefStatus("0");
        customer.setVersion(0);
        customer.setDel(0);
        customer.setAddTime(1L);
        customer.setUpdateTime(1L);

        repository.insert(customer);

        assertEquals(101L, customer.getId());
    }

    @Test
    void should_prepare_typed_query_into_condition_map() {
        CustomerMapper customerMapper = mock(CustomerMapper.class);
        when(customerMapper.findByCondition(anyMap())).thenReturn(java.util.List.of());

        CustomerRepositoryImpl repository = new CustomerRepositoryImpl(customerMapper);
        CustomerQueryPojo queryPojo = new CustomerQueryPojo();
        queryPojo.setCorpid("corp-001");
        queryPojo.setKeyword("杭州");
        queryPojo.setPageNum(2);
        queryPojo.setPageSize(10);

        repository.findByCondition(queryPojo);

        verify(customerMapper).findByCondition(Map.of(
            "corpid", "corp-001",
            "keyword", "杭州",
            "pageNum", 2,
            "pageSize", 10,
            "offset", 10
        ));
    }
}
