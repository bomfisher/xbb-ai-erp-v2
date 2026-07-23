package xbb.ai.erp.module.customer.infrastructure.persistence.convertor;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;
import xbb.ai.erp.module.customer.infrastructure.persistence.po.CustomerInvoiceProfilePO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CustomerInvoiceProfileConvertorTest {

    @Test
    void should_convert_between_invoice_profile_and_po() {
        CustomerInvoiceProfile domain = new CustomerInvoiceProfile();
        domain.setId(1L);
        domain.setCorpid("corp-001");
        domain.setInvoiceTitle("杭州某客户有限公司");
        domain.setTaxNo("91330100TEST001");

        CustomerInvoiceProfilePO po = CustomerInvoiceProfileConvertor.toPO(domain);
        CustomerInvoiceProfile converted = CustomerInvoiceProfileConvertor.toDomain(po);

        assertEquals(1L, po.getId());
        assertEquals("corp-001", po.getCorpid());
        assertEquals("杭州某客户有限公司", converted.getInvoiceTitle());
        assertEquals("91330100TEST001", converted.getTaxNo());
    }
}
