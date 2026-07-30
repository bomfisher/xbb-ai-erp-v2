package xbb.ai.erp.module.customer.domain.repository;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.pojo.CustomerAddressQueryPojo;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerAddressRepositorySignatureTest {

    @Test
    void should_declare_address_repository_methods() throws Exception {
        Method insert = CustomerAddressRepository.class.getMethod(
            "insert",
            xbb.ai.erp.module.customer.domain.model.CustomerAddress.class
        );
        Method findById = CustomerAddressRepository.class.getMethod("findById", String.class, Long.class);
        Method findByCondition = CustomerAddressRepository.class.getMethod("findByCondition", CustomerAddressQueryPojo.class);

        assertNotNull(insert);
        assertNotNull(findById);
        assertNotNull(findByCondition);
    }
}
