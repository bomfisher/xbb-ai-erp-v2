package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.customer.admin.dto.CustomerAddressItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerBankAccountItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerContactItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerInvoiceProfileItemDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerMainDTO;
import xbb.ai.erp.module.customer.admin.dto.CustomerSaveDTO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerAddressRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerBankAccountRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerContactRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.application.service.support.InMemoryCustomerRepository;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerSaveValidationTest {

    @Test
    void should_reject_multiple_default_contacts() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository()
        );

        CustomerContactItemDTO contactA = new CustomerContactItemDTO();
        contactA.setContactName("张三");
        contactA.setDefaultFlag(1);

        CustomerContactItemDTO contactB = new CustomerContactItemDTO();
        contactB.setContactName("李四");
        contactB.setDefaultFlag(1);

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("DRAFT");

        CustomerSaveDTO dto = new CustomerSaveDTO();
        dto.setCorpid("corp-001");
        dto.setMain(main);
        dto.setContacts(List.of(contactA, contactB));

        assertThrows(IllegalArgumentException.class, () -> service.save(dto));
    }

    @Test
    void should_reject_removing_default_contact_on_full_save() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryCustomerContactRepository contactRepository = new InMemoryCustomerContactRepository();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customerRepository.seed(customer);

        CustomerContact contact = new CustomerContact();
        contact.setId(10L);
        contact.setCorpid("corp-001");
        contact.setCustomerId(1L);
        contact.setContactName("默认联系人");
        contact.setDefaultFlag(1);
        contactRepository.seed(contact);

        CustomerSaveDTO dto = buildBaseSaveDTO();
        dto.getMain().setId(1L);
        dto.setContacts(List.of());

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            contactRepository,
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository()
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.save(dto));
        assertEquals("默认联系人不允许通过整单保存删除", ex.getMessage());
    }

    @Test
    void should_reject_removing_default_address_on_full_save() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryCustomerAddressRepository addressRepository = new InMemoryCustomerAddressRepository();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customerRepository.seed(customer);

        CustomerAddress address = new CustomerAddress();
        address.setId(20L);
        address.setCorpid("corp-001");
        address.setCustomerId(1L);
        address.setReceiverName("默认地址");
        address.setDefaultFlag(1);
        addressRepository.seed(address);

        CustomerSaveDTO dto = buildBaseSaveDTO();
        dto.getMain().setId(1L);
        dto.setAddresses(List.of());

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            new InMemoryCustomerContactRepository(),
            addressRepository,
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository()
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.save(dto));
        assertEquals("默认地址不允许通过整单保存删除", ex.getMessage());
    }

    @Test
    void should_reject_removing_default_bank_account_on_full_save() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryCustomerBankAccountRepository bankAccountRepository = new InMemoryCustomerBankAccountRepository();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customerRepository.seed(customer);

        CustomerBankAccount bankAccount = new CustomerBankAccount();
        bankAccount.setId(30L);
        bankAccount.setCorpid("corp-001");
        bankAccount.setCustomerId(1L);
        bankAccount.setAccountName("默认银行账户");
        bankAccount.setDefaultFlag(1);
        bankAccountRepository.seed(bankAccount);

        CustomerSaveDTO dto = buildBaseSaveDTO();
        dto.getMain().setId(1L);
        dto.setBankAccounts(List.of());

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            bankAccountRepository,
            new InMemoryCustomerInvoiceProfileRepository()
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.save(dto));
        assertEquals("默认银行账户不允许通过整单保存删除", ex.getMessage());
    }

    @Test
    void should_reject_removing_default_invoice_profile_on_full_save() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryCustomerInvoiceProfileRepository invoiceProfileRepository = new InMemoryCustomerInvoiceProfileRepository();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customerRepository.seed(customer);

        CustomerInvoiceProfile invoiceProfile = new CustomerInvoiceProfile();
        invoiceProfile.setId(40L);
        invoiceProfile.setCorpid("corp-001");
        invoiceProfile.setCustomerId(1L);
        invoiceProfile.setInvoiceTitle("默认开票信息");
        invoiceProfile.setDefaultFlag(1);
        invoiceProfileRepository.seed(invoiceProfile);

        CustomerSaveDTO dto = buildBaseSaveDTO();
        dto.getMain().setId(1L);
        dto.setInvoiceProfiles(List.of());

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            invoiceProfileRepository
        );

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.save(dto));
        assertEquals("默认开票信息不允许通过整单保存删除", ex.getMessage());
    }

    private CustomerSaveDTO buildBaseSaveDTO() {
        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("DRAFT");

        CustomerSaveDTO dto = new CustomerSaveDTO();
        dto.setCorpid("corp-001");
        dto.setMain(main);
        dto.setContacts(List.of());
        dto.setAddresses(List.of());
        dto.setBankAccounts(List.of());
        dto.setInvoiceProfiles(List.of());
        return dto;
    }
}
