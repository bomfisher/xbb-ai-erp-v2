package xbb.ai.erp.module.customer.infrastructure.persistence.convertor;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerPO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerConvertorTest {

    @Test
    void should_convert_between_customer_and_po() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");

        CustomerPO customerPO = CustomerConvertor.toPO(customer);
        Customer converted = CustomerConvertor.toDomain(customerPO);

        assertEquals(1L, customerPO.getId());
        assertEquals("corp-001", customerPO.getCorpid());
        assertEquals("CUST-001", converted.getCustomerCode());
    }
}
