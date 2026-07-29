package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.exception.BizException;
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
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.model.CustomerBankAccount;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CustomerSaveValidationTest {

    @Test
    void should_allow_incomplete_payload_for_draft_but_reject_submit() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerName("仅草稿");

        CustomerDraftSaveDTO draftDTO = new CustomerDraftSaveDTO();
        draftDTO.setCorpid("corp-001");
        draftDTO.setMain(main);

        CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setMain(main);

        assertDoesNotThrow(() -> service.saveDraft(draftDTO));
        BizException ex = assertThrows(BizException.class, () -> service.saveAndSubmit(submitDTO));
        assertEquals("客户编码不能为空", ex.getMessage());
    }

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

        assertThrows(BizException.class, () -> service.save(dto));
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

        BizException ex = assertThrows(BizException.class, () -> service.save(dto));
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

        BizException ex = assertThrows(BizException.class, () -> service.save(dto));
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

        BizException ex = assertThrows(BizException.class, () -> service.save(dto));
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

        BizException ex = assertThrows(BizException.class, () -> service.save(dto));
        assertEquals("默认开票信息不允许通过整单保存删除", ex.getMessage());
    }

    @Test
    void should_allow_missing_required_fields_for_draft_when_common_rules_pass() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerName("仅草稿");
        main.setBizStatus("DRAFT");

        CustomerDraftSaveDTO draftDTO = new CustomerDraftSaveDTO();
        draftDTO.setCorpid("corp-001");
        draftDTO.setMain(main);

        assertDoesNotThrow(() -> service.saveDraft(draftDTO));
    }

    @Test
    void should_reject_draft_when_text_length_exceeds_limit() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("x".repeat(201));

        CustomerDraftSaveDTO draftDTO = new CustomerDraftSaveDTO();
        draftDTO.setCorpid("corp-001");
        draftDTO.setMain(main);

        BizException ex = assertThrows(BizException.class, () -> service.saveDraft(draftDTO));
        assertEquals("客户名称长度不能超过200", ex.getMessage());
    }

    @Test
    void should_reject_submit_when_default_flag_format_is_invalid() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("1");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setContactName("张三");
        contact.setDefaultFlag(2);

        CustomerAddressItemDTO address = new CustomerAddressItemDTO();
        address.setAddressType("DELIVERY");
        address.setDetailAddress("文三路1号");

        CustomerBankAccountItemDTO bankAccount = new CustomerBankAccountItemDTO();
        bankAccount.setAccountName("杭州客户");
        bankAccount.setBankName("招商银行");
        bankAccount.setAccountNo("6222000000000000");

        CustomerInvoiceProfileItemDTO invoiceProfile = new CustomerInvoiceProfileItemDTO();
        invoiceProfile.setInvoiceTitle("杭州客户有限公司");
        invoiceProfile.setTaxNo("91330100XXXX");

        CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setMain(main);
        submitDTO.getExt().setContacts(List.of(contact));
        submitDTO.getExt().setAddresses(List.of(address));
        submitDTO.getExt().setBankAccounts(List.of(bankAccount));
        submitDTO.getExt().setInvoiceProfiles(List.of(invoiceProfile));

        BizException ex = assertThrows(BizException.class, () -> service.saveAndSubmit(submitDTO));
        assertEquals("是否默认格式不合法", ex.getMessage());
    }

    @Test
    void should_reject_submit_when_required_contact_name_is_blank() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("1");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setContactName("   ");
        contact.setDefaultFlag(1);

        CustomerAddressItemDTO address = new CustomerAddressItemDTO();
        address.setAddressType("DELIVERY");
        address.setDetailAddress("文三路1号");

        CustomerBankAccountItemDTO bankAccount = new CustomerBankAccountItemDTO();
        bankAccount.setAccountName("杭州客户");
        bankAccount.setBankName("招商银行");
        bankAccount.setAccountNo("6222000000000000");

        CustomerInvoiceProfileItemDTO invoiceProfile = new CustomerInvoiceProfileItemDTO();
        invoiceProfile.setInvoiceTitle("杭州客户有限公司");
        invoiceProfile.setTaxNo("91330100XXXX");

        CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setMain(main);
        submitDTO.getExt().setContacts(List.of(contact));
        submitDTO.getExt().setAddresses(List.of(address));
        submitDTO.getExt().setBankAccounts(List.of(bankAccount));
        submitDTO.getExt().setInvoiceProfiles(List.of(invoiceProfile));

        BizException ex = assertThrows(BizException.class, () -> service.saveAndSubmit(submitDTO));
        assertEquals("联系人姓名不能为空", ex.getMessage());
    }

    @Test
    void should_allow_submit_when_optional_child_sections_are_empty() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("1");

        CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setMain(main);

        assertDoesNotThrow(() -> service.saveAndSubmit(submitDTO));
    }

    @Test
    void should_allow_submit_when_contact_section_is_closed_even_if_history_row_exists() throws Exception {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("1");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setMobile("13800000000");

        CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setMain(main);
        submitDTO.getExt().setContacts(List.of(contact));
        writeField(readField(submitDTO, "sectionState"), "contacts", 0);
        writeField(readField(submitDTO, "sectionState"), "addresses", 0);
        writeField(readField(submitDTO, "sectionState"), "bankAccounts", 0);
        writeField(readField(submitDTO, "sectionState"), "invoiceProfiles", 0);

        assertDoesNotThrow(() -> service.saveAndSubmit(submitDTO));
    }

    @Test
    void should_reject_submit_when_contact_section_started_but_required_name_is_blank() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("1");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setMobile("13800000000");

        CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setMain(main);
        submitDTO.getExt().setContacts(List.of(contact));

        BizException ex = assertThrows(BizException.class, () -> service.saveAndSubmit(submitDTO));
        assertEquals("联系人姓名不能为空", ex.getMessage());
    }

    @Test
    void should_reject_submit_when_add_item_required_customer_category_is_blank() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new InMemoryCustomerRepository(),
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("   ");

        CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setMain(main);

        BizException ex = assertThrows(BizException.class, () -> service.saveAndSubmit(submitDTO));
        assertEquals("客户分类不能为空", ex.getMessage());
    }

    @Test
    void should_reject_submit_when_customer_code_is_duplicated() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        Customer existed = new Customer();
        existed.setId(1L);
        existed.setCorpid("corp-001");
        existed.setCustomerCode("CUST-001");
        customerRepository.seed(existed);

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("1");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setContactName("张三");

        CustomerAddressItemDTO address = new CustomerAddressItemDTO();
        address.setAddressType("DELIVERY");
        address.setDetailAddress("文三路1号");

        CustomerBankAccountItemDTO bankAccount = new CustomerBankAccountItemDTO();
        bankAccount.setAccountName("杭州客户");
        bankAccount.setBankName("招商银行");
        bankAccount.setAccountNo("6222000000000000");

        CustomerInvoiceProfileItemDTO invoiceProfile = new CustomerInvoiceProfileItemDTO();
        invoiceProfile.setInvoiceTitle("杭州客户有限公司");
        invoiceProfile.setTaxNo("91330100XXXX");

        CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setMain(main);
        submitDTO.getExt().setContacts(List.of(contact));
        submitDTO.getExt().setAddresses(List.of(address));
        submitDTO.getExt().setBankAccounts(List.of(bankAccount));
        submitDTO.getExt().setInvoiceProfiles(List.of(invoiceProfile));

        BizException ex = assertThrows(BizException.class, () -> service.saveAndSubmit(submitDTO));
        assertEquals("客户编码已存在", ex.getMessage());
    }

    @Test
    void should_allow_submit_when_updating_same_customer_code() {
        InMemoryCustomerRepository customerRepository = new InMemoryCustomerRepository();
        Customer existed = new Customer();
        existed.setId(1L);
        existed.setCorpid("corp-001");
        existed.setCustomerCode("CUST-001");
        customerRepository.seed(existed);

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            customerRepository,
            new InMemoryCustomerContactRepository(),
            new InMemoryCustomerAddressRepository(),
            new InMemoryCustomerBankAccountRepository(),
            new InMemoryCustomerInvoiceProfileRepository(),
            new InMemoryCustomerDraftRepository()
        );

        CustomerMainDTO main = new CustomerMainDTO();
        main.setId(1L);
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("1");

        CustomerContactItemDTO contact = new CustomerContactItemDTO();
        contact.setContactName("张三");

        CustomerAddressItemDTO address = new CustomerAddressItemDTO();
        address.setAddressType("DELIVERY");
        address.setDetailAddress("文三路1号");

        CustomerBankAccountItemDTO bankAccount = new CustomerBankAccountItemDTO();
        bankAccount.setAccountName("杭州客户");
        bankAccount.setBankName("招商银行");
        bankAccount.setAccountNo("6222000000000000");

        CustomerInvoiceProfileItemDTO invoiceProfile = new CustomerInvoiceProfileItemDTO();
        invoiceProfile.setInvoiceTitle("杭州客户有限公司");
        invoiceProfile.setTaxNo("91330100XXXX");

        CustomerSubmitSaveDTO submitDTO = new CustomerSubmitSaveDTO();
        submitDTO.setCorpid("corp-001");
        submitDTO.setMain(main);
        submitDTO.getExt().setContacts(List.of(contact));
        submitDTO.getExt().setAddresses(List.of(address));
        submitDTO.getExt().setBankAccounts(List.of(bankAccount));
        submitDTO.getExt().setInvoiceProfiles(List.of(invoiceProfile));

        assertDoesNotThrow(() -> service.saveAndSubmit(submitDTO));
    }

    private CustomerSaveDTO buildBaseSaveDTO() {
        CustomerMainDTO main = new CustomerMainDTO();
        main.setCustomerCode("CUST-001");
        main.setCustomerName("杭州客户");
        main.setCustomerCategory("A");
        main.setBizStatus("1");

        CustomerSaveDTO dto = new CustomerSaveDTO();
        dto.setCorpid("corp-001");
        dto.setMain(main);
        return dto;
    }

    private Object readField(Object target, String name) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        return field.get(target);
    }

    private void writeField(Object target, String name, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(name);
        field.setAccessible(true);
        field.set(target, value);
    }
}
