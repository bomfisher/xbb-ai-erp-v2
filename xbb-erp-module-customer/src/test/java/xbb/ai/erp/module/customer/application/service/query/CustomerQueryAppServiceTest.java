package xbb.ai.erp.module.customer.application.service.query;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.dto.CustomerListDTO;
import xbb.ai.erp.module.customer.admin.vo.CustomerListItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.field.DefaultCustomerFieldFactory;
import xbb.ai.erp.module.customer.application.schema.CustomerListQueryAdapter;
import xbb.ai.erp.module.customer.application.schema.CustomerListSchemaProvider;
import xbb.ai.erp.module.customer.application.service.support.FakeCustomerContactRepository;
import xbb.ai.erp.module.customer.application.service.support.FakeCustomerRepository;
import xbb.ai.erp.module.customer.domain.model.Customer;
import xbb.ai.erp.module.customer.domain.model.CustomerContact;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerQueryAppServiceTest {

    @Test
    void should_support_list_and_form_views() {
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
        contact.setDefaultFlag(1);

        CustomerQueryAppService service = new CustomerQueryAppServiceImpl(
            new FakeCustomerRepository(List.of(customer)),
            new FakeCustomerContactRepository(List.of(contact)),
            null,
            null,
            null,
            new DefaultCustomerFieldFactory(List.of()),
            new CustomerListQueryAdapter(new CustomerListSchemaProvider())
        );

        CustomerListDTO listDTO = new CustomerListDTO();
        listDTO.setCorpid("corp-001");
        listDTO.setPageNum(1);
        listDTO.setPageSize(20);

        ListBaseVO<CustomerListItemVO> listResult = service.list(listDTO);
        assertFalse(listResult.getList().isEmpty());
        assertEquals("张三", listResult.getList().get(0).getDefaultContactName());

        SaveItemVO<CustomerSaveItemVO> addItemResult = service.addItem(new BaseDTO());
        assertNotNull(addItemResult.getHeadList());

        IdBaseDTO detailDTO = new IdBaseDTO();
        detailDTO.setCorpid("corp-001");
        detailDTO.setId(1L);
        SaveItemVO<CustomerSaveItemVO> updateItemResult = service.updateItem(detailDTO);
        assertEquals("CUST-001", updateItemResult.getData().getMain().getCustomerCode());

        FieldEntity bizStatusField = updateItemResult.getHeadList().stream()
            .filter(field -> "main.bizStatus".equals(field.getAttr()))
            .findFirst()
            .orElseThrow();
        assertEquals(2, bizStatusField.getItemList().size());
    }
}
