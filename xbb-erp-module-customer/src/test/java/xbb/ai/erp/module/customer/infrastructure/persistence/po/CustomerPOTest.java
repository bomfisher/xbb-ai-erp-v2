package xbb.ai.erp.module.customer.infrastructure.persistence.po;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.persistence.entity.BaseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerPOTest {

    @Test
    void should_extend_base_entity_and_hold_customer_fields() {
        CustomerPO customerPO = new CustomerPO();
        customerPO.setCorpid("corp-001");
        customerPO.setCustomerCode("CUST-001");
        customerPO.setDel(0);

        assertTrue(customerPO instanceof BaseEntity);
        assertEquals("corp-001", customerPO.getCorpid());
        assertEquals("CUST-001", customerPO.getCustomerCode());
        assertEquals(0, customerPO.getDel());
    }
}
