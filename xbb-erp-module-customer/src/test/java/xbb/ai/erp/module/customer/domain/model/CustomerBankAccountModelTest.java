package xbb.ai.erp.module.customer.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerBankAccountModelTest {

    @Test
    void should_hold_bank_account_fields() {
        CustomerBankAccount bankAccount = new CustomerBankAccount();
        bankAccount.setCorpid("corp-001");
        bankAccount.setCustomerId(10L);
        bankAccount.setAccountNo("62220001");
        bankAccount.setDefaultFlag(1);

        assertEquals("corp-001", bankAccount.getCorpid());
        assertEquals(10L, bankAccount.getCustomerId());
        assertEquals("62220001", bankAccount.getAccountNo());
        assertEquals(1, bankAccount.getDefaultFlag());
    }
}
