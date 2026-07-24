package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;
import xbb.ai.erp.module.customer.application.service.support.FakeCustomerContactRepository;
import xbb.ai.erp.module.customer.application.service.support.FakeCustomerRepository;
import xbb.ai.erp.module.customer.domain.field.CustomerFieldRule;
import xbb.ai.erp.module.customer.domain.field.DefaultCustomerFieldFactory;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;
import xbb.ai.erp.module.customer.domain.repository.CustomerContactRepository;
import xbb.ai.erp.module.customer.domain.repository.CustomerRepository;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerListServiceTest {

    @Test
    void should_aggregate_default_contact_into_list_item() {
        Customer customer = new Customer();
        customer.setId(1L);
        customer.setCorpid("corp-001");
        customer.setCustomerCode("CUST-001");
        customer.setCustomerName("杭州客户");
        customer.setBizStatus("ENABLED");

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
    void should_use_field_factory_for_list_head() {
        CustomerFieldRule extensionRule = fields -> {
            fields.add(new xbb.ai.erp.module.customer.domain.field.CustomerFieldMeta("main.customLevel", "客户等级", FieldTypeEnum.TEXT.getType(), 0, 1));
            return fields;
        };
        DefaultCustomerFieldFactory fieldFactory = new DefaultCustomerFieldFactory(List.of(extensionRule));
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(
            new FakeCustomerRepository(List.of()),
            new FakeCustomerContactRepository(List.of()),
            null,
            null,
            null,
            fieldFactory
        );
        CustomerListDTO dto = new CustomerListDTO();
        dto.setCorpid("corp-001");
        dto.setPageNum(1);
        dto.setPageSize(20);

        ListBaseVO<CustomerListItemVO> result = service.list(dto);

        assertTrue(result.getHeadList().stream().anyMatch(field -> "main.customLevel".equals(field.getAttr())));
    }
}
