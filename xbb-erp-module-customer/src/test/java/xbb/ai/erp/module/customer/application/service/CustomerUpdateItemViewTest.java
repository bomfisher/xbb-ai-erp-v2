package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerUpdateItemViewTest {

    @Test
    void should_return_existing_data_structure_for_update_item() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(null, null, null, null, null);
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");
        dto.setId(1L);

        SaveItemVO<CustomerSaveItemVO> result = service.updateItem(dto);

        assertNotNull(result.getHeadList());
        assertNotNull(result.getData());
        FieldEntity bizStatusField = result.getHeadList().stream()
            .filter(field -> "main.bizStatus".equals(field.getAttr()))
            .findFirst()
            .orElseThrow();
        assertEquals(2, bizStatusField.getItemList().size());
        assertEquals("0", String.valueOf(bizStatusField.getItemList().get(1).getValue()));
        assertEquals("停用", bizStatusField.getItemList().get(1).getText());
    }

    @Test
    void should_return_existing_customer_data_for_update_item() {
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
        customer.setBizStatus("1");
        customer.setDefaultContactId(11L);
        customer.setDefaultAddressId(21L);
        customer.setDefaultBankAccountId(31L);
        customer.setDefaultInvoiceProfileId(41L);
        customerRepository.seed(customer);

        CustomerContact contact = new CustomerContact();
        contact.setId(11L);
        contact.setCorpid("corp-001");
        contact.setCustomerId(1L);
        contact.setContactName("张三");
        contact.setMobile("13800000000");
        contact.setDefaultFlag(1);
        contactRepository.seed(contact);

        CustomerAddress address = new CustomerAddress();
        address.setId(21L);
        address.setCorpid("corp-001");
        address.setCustomerId(1L);
        address.setAddressType("DELIVERY");
        address.setDetailAddress("文三路 1 号");
        address.setDefaultFlag(1);
        addressRepository.seed(address);

        CustomerBankAccount bankAccount = new CustomerBankAccount();
        bankAccount.setId(31L);
        bankAccount.setCorpid("corp-001");
        bankAccount.setCustomerId(1L);
        bankAccount.setAccountName("杭州客户");
        bankAccount.setBankName("招商银行");
        bankAccount.setAccountNo("6222000000000000");
        bankAccount.setDefaultFlag(1);
        bankAccountRepository.seed(bankAccount);

        CustomerInvoiceProfile invoiceProfile = new CustomerInvoiceProfile();
        invoiceProfile.setId(41L);
        invoiceProfile.setCorpid("corp-001");
        invoiceProfile.setCustomerId(1L);
        invoiceProfile.setInvoiceTitle("杭州客户有限公司");
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
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");
        dto.setId(1L);

        SaveItemVO<CustomerSaveItemVO> result = service.updateItem(dto);

        assertEquals("CUST-001", result.getData().getMain().getCustomerCode());
        assertEquals("杭州客户", result.getData().getMain().getCustomerName());
        assertEquals("1", result.getData().getMain().getBizStatus());
        assertEquals(1, result.getData().getContacts().size());
        assertEquals("张三", result.getData().getContacts().get(0).getContactName());
        assertEquals(1, result.getData().getAddresses().size());
        assertEquals("文三路 1 号", result.getData().getAddresses().get(0).getDetailAddress());
        assertEquals(1, result.getData().getBankAccounts().size());
        assertEquals("招商银行", result.getData().getBankAccounts().get(0).getBankName());
        assertEquals(1, result.getData().getInvoiceProfiles().size());
        assertEquals("杭州客户有限公司", result.getData().getInvoiceProfiles().get(0).getInvoiceTitle());
        assertEquals(1, result.getData().getSectionState().getContacts());
        assertEquals(1, result.getData().getSectionState().getAddresses());
        assertEquals(1, result.getData().getSectionState().getBankAccounts());
        assertEquals(1, result.getData().getSectionState().getInvoiceProfiles());
    }
}
