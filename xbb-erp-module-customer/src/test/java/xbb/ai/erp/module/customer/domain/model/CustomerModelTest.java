package xbb.ai.erp.module.customer.domain.model;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.pojo.CustomerAddressQueryPojo;
import xbb.ai.erp.module.customer.domain.pojo.CustomerContactQueryPojo;
import xbb.ai.erp.module.customer.domain.pojo.CustomerQueryPojo;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerModelTest {

    @Test
    void should_hold_customer_fields() {
        Customer customer = new Customer();
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customer.setCustomerName("杭州客户");

        assertEquals("corp-001", customer.getCorpid());
        assertEquals("CUST-001", customer.getCustomerCode());
        assertEquals("杭州客户", customer.getCustomerName());
    }

    @Test
    void should_hold_contact_and_address_query_fields() {
        CustomerContactQueryPojo contactQuery = new CustomerContactQueryPojo();
        contactQuery.setCorpid("corp-001");
        contactQuery.setDefaultFlag(1);

        CustomerAddressQueryPojo addressQuery = new CustomerAddressQueryPojo();
        addressQuery.setCorpid("corp-001");
        addressQuery.setAddressType("DELIVERY");

        CustomerQueryPojo customerQuery = new CustomerQueryPojo();
        customerQuery.setCorpid("corp-001");
        customerQuery.setCustomerCode("CUST-001");

        assertEquals(1, contactQuery.getDefaultFlag());
        assertEquals("DELIVERY", addressQuery.getAddressType());
        assertEquals("CUST-001", customerQuery.getCustomerCode());
    }
}
