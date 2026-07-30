package xbb.ai.erp.module.customer.domain.repository;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerRepositorySignatureTest {

    @Test
    void should_declare_customer_repository_methods() throws Exception {
        Method insert = CustomerRepository.class.getMethod("insert", xbb.ai.erp.module.customer.domain.model.Customer.class);
        Method findById = CustomerRepository.class.getMethod("findById", String.class, Long.class);
        Method findByCondition = CustomerRepository.class.getMethod("findByCondition", CustomerQueryPojo.class);
        Method existsByCustomerCode = CustomerRepository.class.getMethod("existsByCustomerCode", String.class, String.class, Long.class);

        assertNotNull(insert);
        assertNotNull(findById);
        assertNotNull(findByCondition);
        assertNotNull(existsByCustomerCode);
        assertEquals(boolean.class, existsByCustomerCode.getReturnType());
    }
}
