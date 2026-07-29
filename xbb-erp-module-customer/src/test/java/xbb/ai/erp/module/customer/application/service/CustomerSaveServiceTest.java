package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.admin.dto.CustomerAddressItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerBankAccountItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerContactItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerDraftSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerInvoiceProfileItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSubmitSaveDTO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerAddressRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerBankAccountRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerContactRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerDraftRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerRepository;
import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
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
        main.setBizStatus("1");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setContactName("张三");
        contact.setDefaultFlag(1);

        CustomerSaveDTO dto = new CustomerSaveDTO();
        dto.setCorpid("corp-001");
        dto.setMain(main);
        dto.setContacts(List.of(contact));

        Long customerId = service.save(dto);

        assertEquals(1, customerRepository.all().size());
        Customer saved = customerRepository.all().get(0);
        assertEquals(1, contactRepository.all().size());
        assertEquals(customerId, contactRepository.all().get(0).getCustomerId());
        assertNotNull(saved.getId());
        assertEquals("1", saved.getBizStatus());
        assertEquals("0", saved.getRefStatus());
        assertEquals("A", saved.getCustomerCategory());
        assertEquals(0, saved.getVersion());
        assertEquals(0, saved.getDel());
        assertNotNull(saved.getAddTime());
        assertNotNull(saved.getUpdateTime());
    }

    @Test
    void should_apply_insert_defaults_for_all_customer_children() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryCustomerContactRepository contactRepository = new InMemoryCustomerContactRepository();
        InMemoryCustomerAddressRepository addressRepository = new InMemoryCustomerAddressRepository();
        InMemoryCustomerBankAccountRepository bankAccountRepository = new InMemoryCustomerBankAccountRepository();
        InMemoryCustomerInvoiceProfileRepository invoiceProfileRepository = new InMemoryCustomerInvoiceProfileRepository();
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            contactRepository,
            addressRepository,
            bankAccountRepository,
            invoiceProfileRepository
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("1");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setContactName("张三");
        contact.setMobile("13800000000");
        contact.setDefaultFlag(1);

        CustomerAddressItemDTO address = new CustomerAddressItemDTO();
        address.setAddressType("DELIVERY");
        address.setReceiverName("李四");
        address.setReceiverMobile("13900000000");
        address.setDetailAddress("西湖区 1 号");
        address.setDefaultFlag(1);

        CustomerBankAccountItemDTO bankAccount = new CustomerBankAccountItemDTO();
        bankAccount.setAccountName("杭州客户");
        bankAccount.setBankName("招商银行");
        bankAccount.setAccountNo("6222000000000000");
        bankAccount.setAccountUsage("SETTLEMENT");
        bankAccount.setDefaultFlag(1);

        CustomerInvoiceProfileItemDTO invoiceProfile = new CustomerInvoiceProfileItemDTO();
        invoiceProfile.setInvoiceTitle("杭州客户有限公司");
        invoiceProfile.setTaxNo("91330100TEST0001");
        invoiceProfile.setAddressPhone("西湖区 1 号 0571-88888888");
        invoiceProfile.setBankName("招商银行");
        invoiceProfile.setBankAccountNo("6222000000000000");
        invoiceProfile.setDefaultFlag(1);

        CustomerSaveDTO dto = new CustomerSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        dto.setMain(main);
        dto.setContacts(List.of(contact));
        dto.setAddresses(List.of(address));
        dto.setBankAccounts(List.of(bankAccount));
        dto.setInvoiceProfiles(List.of(invoiceProfile));

        service.save(dto);

        assertChildDefaults(contactRepository.all().get(0), "user-001");
        assertChildDefaults(addressRepository.all().get(0), "user-001");
        assertChildDefaults(bankAccountRepository.all().get(0), "user-001");
        assertChildDefaults(invoiceProfileRepository.all().get(0), "user-001");
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
        main.setBizStatus("1");

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

    @Test
    void should_remove_draft_after_submit_success() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryCustomerContactRepository contactRepository = new InMemoryCustomerContactRepository();
        InMemoryCustomerDraftRepository draftRepository = new InMemoryCustomerDraftRepository();
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            contactRepository,
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            draftRepository
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("1");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setContactName("张三");
        contact.setDefaultFlag(1);

        CustomerDraftSaveDTO draftDTO = new CustomerDraftSaveDTO();
        draftDTO.setCorpid("corp-001");
        draftDTO.setMain(main);
        draftDTO.getExt().setContacts(List.of(contact));
        draftDTO.getDraftMeta().setDraftTitle("草稿客户");
        draftDTO.getDraftMeta().setUpdatedTime(100L);
        service.saveDraft(draftDTO);

        String draftCode = draftRepository.listDrafts("corp-001", 10).get(0).getDraftCode();

        CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setMain(main);
        submitDTO.getExt().setContacts(List.of(contact));
        submitDTO.getDraftMeta().setDraftCode(draftCode);

        service.saveAndSubmit(submitDTO);

        assertEquals(1, customerRepository.all().size());
        assertEquals(1, contactRepository.all().size());
        assertTrue(draftRepository.listDrafts("corp-001", 10).isEmpty());
    }

    private void assertChildDefaults(CustomerContact contact, String userId) {
        assertNotNull(contact.getId());
        assertEquals("1", contact.getBizStatus());
        assertEquals(userId, contact.getCreatorId());
        assertEquals(userId, contact.getModifyId());
        assertEquals(0, contact.getVersion());
        assertEquals(0, contact.getDel());
        assertNotNull(contact.getAddTime());
        assertNotNull(contact.getUpdateTime());
    }

    private void assertChildDefaults(CustomerAddress address, String userId) {
        assertNotNull(address.getId());
        assertEquals("1", address.getBizStatus());
        assertEquals(userId, address.getCreatorId());
        assertEquals(userId, address.getModifyId());
        assertEquals(0, address.getVersion());
        assertEquals(0, address.getDel());
        assertNotNull(address.getAddTime());
        assertNotNull(address.getUpdateTime());
    }

    private void assertChildDefaults(CustomerBankAccount bankAccount, String userId) {
        assertNotNull(bankAccount.getId());
        assertEquals("1", bankAccount.getBizStatus());
        assertEquals(userId, bankAccount.getCreatorId());
        assertEquals(userId, bankAccount.getModifyId());
        assertEquals(0, bankAccount.getVersion());
        assertEquals(0, bankAccount.getDel());
        assertNotNull(bankAccount.getAddTime());
        assertNotNull(bankAccount.getUpdateTime());
    }

    private void assertChildDefaults(CustomerInvoiceProfile invoiceProfile, String userId) {
        assertNotNull(invoiceProfile.getId());
        assertEquals("1", invoiceProfile.getBizStatus());
        assertEquals(userId, invoiceProfile.getCreatorId());
        assertEquals(userId, invoiceProfile.getModifyId());
        assertEquals(0, invoiceProfile.getVersion());
        assertEquals(0, invoiceProfile.getDel());
        assertNotNull(invoiceProfile.getAddTime());
        assertNotNull(invoiceProfile.getUpdateTime());
    }
}
