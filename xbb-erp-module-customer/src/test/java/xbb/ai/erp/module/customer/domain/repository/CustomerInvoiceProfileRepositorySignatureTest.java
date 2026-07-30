package xbb.ai.erp.module.customer.domain.repository;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.pojo.CustomerInvoiceProfileQueryPojo;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerInvoiceProfileRepositorySignatureTest {

    @Test
    void should_declare_invoice_profile_repository_methods() throws Exception {
        Method insert = CustomerInvoiceProfileRepository.class.getMethod(
            "insert",
            xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile.class
        );
        Method findById = CustomerInvoiceProfileRepository.class.getMethod("findById", String.class, Long.class);
        Method findByCondition = CustomerInvoiceProfileRepository.class.getMethod("findByCondition", CustomerInvoiceProfileQueryPojo.class);

        assertNotNull(insert);
        assertNotNull(findById);
        assertNotNull(findByCondition);
    }
}
