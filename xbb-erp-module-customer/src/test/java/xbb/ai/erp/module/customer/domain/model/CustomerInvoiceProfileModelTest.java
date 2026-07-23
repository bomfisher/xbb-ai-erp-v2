package xbb.ai.erp.module.customer.domain.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerInvoiceProfileModelTest {

    @Test
    void should_hold_invoice_profile_fields() {
        CustomerInvoiceProfile invoiceProfile = new CustomerInvoiceProfile();
        invoiceProfile.setCorpid("corp-001");
        invoiceProfile.setCustomerId(10L);
        invoiceProfile.setInvoiceTitle("杭州某客户有限公司");
        invoiceProfile.setTaxNo("91330100TEST001");
        invoiceProfile.setDefaultFlag(1);

        assertEquals("corp-001", invoiceProfile.getCorpid());
        assertEquals(10L, invoiceProfile.getCustomerId());
        assertEquals("杭州某客户有限公司", invoiceProfile.getInvoiceTitle());
        assertEquals("91330100TEST001", invoiceProfile.getTaxNo());
        assertEquals(1, invoiceProfile.getDefaultFlag());
    }
}
