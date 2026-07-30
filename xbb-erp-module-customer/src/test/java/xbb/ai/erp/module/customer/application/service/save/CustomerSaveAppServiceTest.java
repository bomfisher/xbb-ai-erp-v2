package xbb.ai.erp.module.customer.application.service.save;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.admin.dto.CustomerContactItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.application.port.CustomerDraftRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerAddressRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerBankAccountRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerContactRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerRepository;
import xbb.ai.erp.module.customer.domain.model.Customer;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerSaveAppServiceTest {

    @Test
    void should_insert_customer_and_contact() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryCustomerContactRepository contactRepository = new InMemoryCustomerContactRepository();
        CustomerSaveAppService service = new CustomerSaveAppServiceImpl(
            customerRepository,
            contactRepository,
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            null
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setContactName("张三");
        contact.setDefaultFlag(1);

        CustomerSaveDTO dto = new CustomerSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);
        dto.setContacts(List.of(contact));

        Long customerId = service.save(dto);

        Customer saved = customerRepository.all().get(0);
        assertNotNull(customerId);
        assertEquals(customerId, contactRepository.all().get(0).getCustomerId());
        assertEquals("1", saved.getBizStatus());
    }
}
