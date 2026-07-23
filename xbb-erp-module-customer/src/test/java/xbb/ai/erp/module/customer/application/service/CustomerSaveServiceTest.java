package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.admin.dto.CustomerContactItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerAddressRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerBankAccountRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerContactRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerRepository;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerSaveServiceTest {

    @Test
    void should_insert_customer_and_contact_in_one_save() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryCustomerContactRepository contactRepository = new InMemoryCustomerContactRepository();
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            contactRepository,
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("ENABLED");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setContactName("张三");
        contact.setDefaultFlag(1);

        CustomerSaveDTO dto = new CustomerSaveDTO();
        dto.setCorpid("corp-001");
        dto.setMain(main);
        dto.setContacts(List.of(contact));

        Long customerId = service.save(dto);

        assertEquals(1, customerRepository.all().size());
        assertEquals(1, contactRepository.all().size());
        assertEquals(customerId, contactRepository.all().get(0).getCustomerId());
    }

    @Test
    void should_keep_existing_contact_when_id_is_present_in_full_save() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryCustomerContactRepository contactRepository = new InMemoryCustomerContactRepository();

        Customer savedCustomer = new Customer();
        savedCustomer.setId(1L);
        savedCustomer.setCorpid("corp-001");
        savedCustomer.setCustomerCode("CUST-001");
        customerRepository.seed(savedCustomer);

        CustomerContact oldContact = new CustomerContact();
        oldContact.setId(10L);
        oldContact.setCorpid("corp-001");
        oldContact.setCustomerId(1L);
        oldContact.setContactName("旧联系人");
        oldContact.setDefaultFlag(0);
        contactRepository.seed(oldContact);

        CustomerMainDTO main = new CustomerMainDTO();
        main.setId(1L);
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("ENABLED");

        CustomerContactItemDTO keptContact = new CustomerContactItemDTO();
        keptContact.setId(10L);
        keptContact.setContactName("新联系人名");
        keptContact.setDefaultFlag(0);

        CustomerSaveDTO dto = new CustomerSaveDTO();
        dto.setCorpid("corp-001");
        dto.setMain(main);
        dto.setContacts(List.of(keptContact));

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            contactRepository,
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository()
        );

        service.save(dto);

        assertEquals(1, contactRepository.all().size());
        assertEquals("新联系人名", contactRepository.all().get(0).getContactName());
    }
}
