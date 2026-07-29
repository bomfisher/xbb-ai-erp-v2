package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.common.admin.pojo.ListFilterCondition;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.support.FakeCustomerAddressRepository;
import xbb.ai.erp.module.customer.application.service.support.FakeCustomerContactRepository;
import xbb.ai.erp.module.customer.application.service.support.FakeCustomerInvoiceProfileRepository;
import xbb.ai.erp.module.customer.application.service.support.FakeCustomerRepository;
import xbb.ai.erp.module.customer.domain.field.DefaultCustomerFieldFactory;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerAddress;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.model.CustomerInvoiceProfile;
import xbb.ai.erp.module.customer.domain.repository.CustomerContactRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerListServiceTest {

    @Test
    void should_aggregate_default_contact_into_list_item() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customer.setCustomerName("杭州客户");
        customer.setBizStatus("1");

        CustomerContact contact = new CustomerContact();
        contact.setCorpid("corp-001");
        contact.setCustomerId(1L);
        contact.setContactName("张三");
        contact.setMobile("13800000000");
        contact.setDefaultFlag(1);

        CustomerRepository customerRepository = new FakeCustomerRepository(List.of(customer));
        CustomerContactRepository contactRepository = new FakeCustomerContactRepository(List.of(contact));

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(customerRepository, contactRepository, null, null, null);
        CustomerListDTO dto = new CustomerListDTO();
        dto.setCorpid("corp-001");
        dto.setPageNum(1);
        dto.setPageSize(20);

        ListBaseVO<CustomerListItemVO> result = service.list(dto);

        assertFalse(result.getList().isEmpty());
        assertEquals("张三", result.getList().get(0).getDefaultContactName());
    }

    @Test
    void should_not_query_default_children_inside_customer_loop() {
        Customer first = buildCustomer(1L, "corp-001", "CUST-001", "杭州客户一", "330100");
        Customer second = buildCustomer(2L, "corp-001", "CUST-002", "杭州客户二", "330100");

        CustomerContact firstContact = new CustomerContact();
        firstContact.setId(11L);
        firstContact.setCorpid("corp-001");
        firstContact.setCustomerId(1L);
        firstContact.setContactName("张三");
        firstContact.setMobile("13800000000");
        firstContact.setDefaultFlag(1);

        CustomerContact secondContact = new CustomerContact();
        secondContact.setId(12L);
        secondContact.setCorpid("corp-001");
        secondContact.setCustomerId(2L);
        secondContact.setContactName("李四");
        secondContact.setMobile("13900000000");
        secondContact.setDefaultFlag(1);

        CustomerAddress firstAddress = new CustomerAddress();
        firstAddress.setId(21L);
        firstAddress.setCorpid("corp-001");
        firstAddress.setCustomerId(1L);
        firstAddress.setDetailAddress("文三路 1 号");
        firstAddress.setDefaultFlag(1);

        CustomerAddress secondAddress = new CustomerAddress();
        secondAddress.setId(22L);
        secondAddress.setCorpid("corp-001");
        secondAddress.setCustomerId(2L);
        secondAddress.setDetailAddress("文二路 2 号");
        secondAddress.setDefaultFlag(1);

        CustomerInvoiceProfile firstInvoice = new CustomerInvoiceProfile();
        firstInvoice.setId(31L);
        firstInvoice.setCorpid("corp-001");
        firstInvoice.setCustomerId(1L);
        firstInvoice.setInvoiceTitle("杭州客户一有限公司");
        firstInvoice.setDefaultFlag(1);

        CustomerInvoiceProfile secondInvoice = new CustomerInvoiceProfile();
        secondInvoice.setId(32L);
        secondInvoice.setCorpid("corp-001");
        secondInvoice.setCustomerId(2L);
        secondInvoice.setInvoiceTitle("杭州客户二有限公司");
        secondInvoice.setDefaultFlag(1);

        FakeCustomerContactRepository contactRepository = new FakeCustomerContactRepository(List.of(firstContact, secondContact));
        FakeCustomerAddressRepository addressRepository = new FakeCustomerAddressRepository(List.of(firstAddress, secondAddress));
        FakeCustomerInvoiceProfileRepository invoiceProfileRepository = new FakeCustomerInvoiceProfileRepository(List.of(firstInvoice, secondInvoice));

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new FakeCustomerRepository(List.of(first, second)),
            contactRepository,
            addressRepository,
            null,
            invoiceProfileRepository,
            new DefaultCustomerFieldFactory(List.of())
        );
        CustomerListDTO dto = new CustomerListDTO();
        dto.setCorpid("corp-001");
        dto.setPageNum(1);
        dto.setPageSize(20);

        ListBaseVO<CustomerListItemVO> result = service.list(dto);

        assertEquals(2, result.getList().size());
        assertEquals("张三", result.getList().get(0).getDefaultContactName());
        assertEquals("文三路 1 号", result.getList().get(0).getDefaultAddressSummary());
        assertEquals("杭州客户一有限公司", result.getList().get(0).getDefaultInvoiceTitle());
        assertEquals(1, contactRepository.getFindByConditionCallCount());
        assertEquals(1, addressRepository.getFindByConditionCallCount());
        assertEquals(1, invoiceProfileRepository.getFindByConditionCallCount());
    }

    @Test
    void should_not_return_head_list_in_list_response() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new FakeCustomerRepository(List.of()),
            new FakeCustomerContactRepository(List.of()),
            null,
            null,
            null,
            new DefaultCustomerFieldFactory(List.of())
        );
        CustomerListDTO dto = new CustomerListDTO();
        dto.setCorpid("corp-001");
        dto.setPageNum(1);
        dto.setPageSize(20);

        ListBaseVO<CustomerListItemVO> result = service.list(dto);

        assertNull(result.getHeadList());
    }

    @Test
    void should_apply_keyword_and_conditions_with_paged_result() {
        Customer first = buildCustomer(1L, "corp-001", "CUST-001", "杭州客户一", "330100");
        Customer second = buildCustomer(2L, "corp-001", "CUST-002", "杭州客户二", "330100");
        Customer third = buildCustomer(3L, "corp-001", "CUST-003", "宁波客户", "330200");

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new FakeCustomerRepository(List.of(first, second, third)),
            new FakeCustomerContactRepository(List.of()),
            null,
            null,
            null,
            new DefaultCustomerFieldFactory(List.of())
        );
        CustomerListDTO dto = new CustomerListDTO();
        dto.setCorpid("corp-001");
        dto.setKeyword("杭州");
        dto.setConditions(List.of(buildCondition("regionCode", "ENUM", "EQ", "330100")));
        dto.setPageNum(1);
        dto.setPageSize(1);

        ListBaseVO<CustomerListItemVO> result = service.list(dto);

        assertEquals(1, result.getList().size());
        assertEquals("CUST-001", result.getList().get(0).getCustomerCode());
        assertEquals(2, result.getPageHelper().getCount());
        assertFalse(result.getPageHelper().getHasLeft());
        assertTrue(result.getPageHelper().getHasRight());
    }

    @Test
    void should_reject_unknown_filter_symbol_in_fake_repository() {
        Customer customer = buildCustomer(1L, "corp-001", "CUST-001", "杭州客户一", "330100");

        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new FakeCustomerRepository(List.of(customer)),
            new FakeCustomerContactRepository(List.of()),
            null,
            null,
            null,
            new DefaultCustomerFieldFactory(List.of())
        );
        CustomerListDTO dto = new CustomerListDTO();
        dto.setCorpid("corp-001");
        dto.setConditions(List.of(buildCondition("customerName", "TEXT", "UNKNOWN", "杭州")));

        assertThrows(BizException.class, () -> service.list(dto));
    }

    @Test
    void should_reject_malformed_conditions_payload_in_fake_repository() {
        FakeCustomerRepository repository = new FakeCustomerRepository(List.of(
            buildCustomer(1L, "corp-001", "CUST-001", "杭州客户一", "330100")
        ));

        assertThrows(BizException.class, () -> repository.findByCondition(Map.of(
            "corpid", "corp-001",
            "conditions", "bad-payload"
        )));
    }

    @Test
    void should_reject_unknown_filter_attr_in_fake_repository_directly() {
        FakeCustomerRepository repository = new FakeCustomerRepository(List.of(
            buildCustomer(1L, "corp-001", "CUST-001", "杭州客户一", "330100")
        ));

        assertThrows(BizException.class, () -> repository.findByCondition(Map.of(
            "corpid", "corp-001",
            "conditions", List.of(buildCondition("unknown_attr", "TEXT", "EQ", "杭州"))
        )));
    }

    @Test
    void should_reject_unknown_filter_symbol_in_fake_repository_directly() {
        FakeCustomerRepository repository = new FakeCustomerRepository(List.of(
            buildCustomer(1L, "corp-001", "CUST-001", "杭州客户一", "330100")
        ));

        assertThrows(BizException.class, () -> repository.findByCondition(Map.of(
            "corpid", "corp-001",
            "conditions", List.of(buildCondition("customer_name", "TEXT", "UNKNOWN", "杭州"))
        )));
    }

    private static Customer buildCustomer(Long id, String corpid, String customerCode, String customerName, String regionCode) {
        Customer customer = new Customer();
        customer.setId(id);
        customer.setCorpid(corpid);
        customer.setCustomerCode(customerCode);
        customer.setCustomerName(customerName);
        customer.setRegionCode(regionCode);
        customer.setBizStatus("1");
        customer.setRefStatus("0");
        return customer;
    }

    private static ListFilterCondition buildCondition(String attr, String fieldType, String symbol, String value) {
        ListFilterCondition condition = new ListFilterCondition();
        condition.setAttr(attr);
        condition.setFieldType(fieldType);
        condition.setSymbol(symbol);
        condition.setValue(List.of(value));
        return condition;
    }
}
