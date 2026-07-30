package xbb.ai.erp.module.customer.domain.repository;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.pojo.CustomerContactQueryPojo;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerContactRepositorySignatureTest {

    @Test
    void should_declare_contact_repository_methods() throws Exception {
        Method insert = CustomerContactRepository.class.getMethod(
            "insert",
            xbb.ai.erp.module.customer.domain.model.CustomerContact.class
        );
        Method findById = CustomerContactRepository.class.getMethod("findById", String.class, Long.class);
        Method findByCondition = CustomerContactRepository.class.getMethod("findByCondition", CustomerContactQueryPojo.class);

        assertNotNull(insert);
        assertNotNull(findById);
        assertNotNull(findByCondition);
    }
}
