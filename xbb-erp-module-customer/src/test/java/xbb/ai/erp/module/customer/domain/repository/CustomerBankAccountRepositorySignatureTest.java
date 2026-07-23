package xbb.ai.erp.module.customer.domain.repository;

import org.junit.jupiter.api.Test;

import java.lang.reflect.Method;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerBankAccountRepositorySignatureTest {

    @Test
    void should_declare_bank_account_repository_methods() throws Exception {
        Method insert = CustomerBankAccountRepository.class.getMethod(
            "insert",
            xbb.ai.erp.module.customer.domain.model.CustomerBankAccount.class
        );
        Method findById = CustomerBankAccountRepository.class.getMethod("findById", String.class, Long.class);
        Method findByCondition = CustomerBankAccountRepository.class.getMethod("findByCondition", Map.class);

        assertNotNull(insert);
        assertNotNull(findById);
        assertNotNull(findByCondition);
    }
}
