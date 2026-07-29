package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerAddItemViewTest {

    @Test
    void should_return_head_list_and_empty_nested_data() {
        CustomerAdminAppServiceImpl service = CustomerAdminAppServiceImpl.forTesting(null, null, null, null, null);
        SaveItemVO<CustomerSaveItemVO> result = service.addItem(new BaseDTO());

        assertNotNull(result.getHeadList());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getMain());
        assertTrue(result.getData().getContacts().isEmpty());
        assertTrue(result.getData().getBankAccounts().isEmpty());
        assertEquals(0, result.getData().getSectionState().getContacts());
        assertEquals(0, result.getData().getSectionState().getAddresses());
        assertEquals(0, result.getData().getSectionState().getBankAccounts());
        assertEquals(0, result.getData().getSectionState().getInvoiceProfiles());
        FieldEntity bizStatusField = result.getHeadList().stream()
            .filter(field -> "main.bizStatus".equals(field.getAttr()))
            .findFirst()
            .orElseThrow();
        assertEquals(2, bizStatusField.getItemList().size());
        assertEquals("1", String.valueOf(bizStatusField.getItemList().get(0).getValue()));
        assertEquals("启用", bizStatusField.getItemList().get(0).getText());
    }
}
