package xbb.ai.erp.module.customer.domain.repository;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerRepositorySignatureTest {

    @Test
    void should_declare_customer_repository_methods() throws Exception {
        Method insert = CustomerRepository.class.getMethod("insert", xbb.ai.erp.module.customer.domain.model.Customer.class);
        Method findById = CustomerRepository.class.getMethod("findById", String.class, Long.class);
        Method findByCondition = CustomerRepository.class.getMethod("findByCondition", Map.class);

        assertNotNull(insert);
        assertNotNull(findById);
        assertNotNull(findByCondition);
    }
}
