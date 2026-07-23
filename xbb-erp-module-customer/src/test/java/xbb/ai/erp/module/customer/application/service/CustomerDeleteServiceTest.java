package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerDeleteServiceTest {

    @Test
    void should_delete_customer_and_all_children() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        InMemoryCustomerContactRepository contactRepository = new InMemoryCustomerContactRepository();
        InMemoryCustomerAddressRepository addressRepository = new InMemoryCustomerAddressRepository();
        InMemoryCustomerBankAccountRepository bankAccountRepository = new InMemoryCustomerBankAccountRepository();
        InMemoryCustomerInvoiceProfileRepository invoiceProfileRepository = new InMemoryCustomerInvoiceProfileRepository();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customer.setCustomerName("杭州客户");
        customer.setBizStatus("ENABLED");
        customerRepository.seed(customer);

        CustomerContact contact = new CustomerContact();
        contact.setId(11L);
        contact.setCorpid("corp-001");
        contact.setCustomerId(1L);
        contact.setContactName("张三");
        contact.setDefaultFlag(1);
        contactRepository.seed(contact);

        CustomerAddress address = new CustomerAddress();
        address.setId(21L);
        address.setCorpid("corp-001");
        address.setCustomerId(1L);
        address.setReceiverName("李四");
        address.setDefaultFlag(1);
        addressRepository.seed(address);

        CustomerBankAccount bankAccount = new CustomerBankAccount();
        bankAccount.setId(31L);
        bankAccount.setCorpid("corp-001");
        bankAccount.setCustomerId(1L);
        bankAccount.setAccountName("杭州客户");
        bankAccount.setAccountNo("62220001");
        bankAccount.setDefaultFlag(1);
        bankAccountRepository.seed(bankAccount);

        CustomerInvoiceProfile invoiceProfile = new CustomerInvoiceProfile();
        invoiceProfile.setId(41L);
        invoiceProfile.setCorpid("corp-001");
        invoiceProfile.setCustomerId(1L);
        invoiceProfile.setInvoiceTitle("杭州客户");
        invoiceProfile.setTaxNo("91330100XXXX");
        invoiceProfile.setDefaultFlag(1);
        invoiceProfileRepository.seed(invoiceProfile);

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            contactRepository,
            addressRepository,
            bankAccountRepository,
            invoiceProfileRepository
        );

        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");
        dto.setIdList(List.of(1L));

        service.delete(dto);

        assertTrue(customerRepository.all().isEmpty());
        assertTrue(contactRepository.all().isEmpty());
        assertTrue(addressRepository.all().isEmpty());
        assertTrue(bankAccountRepository.all().isEmpty());
        assertTrue(invoiceProfileRepository.all().isEmpty());
    }

    @Test
    void should_reject_delete_when_customer_is_referenced() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();

        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customer.setRefStatus("1");
        customerRepository.seed(customer);

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository()
        );

        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");
        dto.setIdList(List.of(1L));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.delete(dto));
        assertEquals("客户已被引用，不能删除", ex.getMessage());
    }

    @Test
    void should_reject_delete_when_customer_not_found() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository()
        );

        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");
        dto.setIdList(List.of(999L));

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> service.delete(dto));
        assertEquals("客户不存在", ex.getMessage());
    }
}
