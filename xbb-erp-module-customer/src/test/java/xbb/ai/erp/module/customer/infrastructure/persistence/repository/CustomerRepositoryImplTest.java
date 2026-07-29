package xbb.ai.erp.module.customer.infrastructure.persistence.repository;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.infrastructure.persistence.mapper.CustomerMapper;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

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
}
