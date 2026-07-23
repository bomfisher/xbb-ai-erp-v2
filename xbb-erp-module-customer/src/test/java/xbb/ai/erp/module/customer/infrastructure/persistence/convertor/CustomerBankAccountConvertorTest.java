package xbb.ai.erp.module.customer.infrastructure.persistence.convertor;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerBankAccountPO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerBankAccountConvertorTest {

    @Test
    void should_convert_between_bank_account_and_po() {
        CustomerBankAccount domain = new CustomerBankAccount();
        domain.setId(1L);
        domain.setCorpid("corp-001");
        domain.setAccountNo("62220001");

        CustomerBankAccountPO po = CustomerBankAccountConvertor.toPO(domain);
        CustomerBankAccount converted = CustomerBankAccountConvertor.toDomain(po);

        assertEquals(1L, po.getId());
        assertEquals("corp-001", po.getCorpid());
        assertEquals("62220001", converted.getAccountNo());
    }
}
