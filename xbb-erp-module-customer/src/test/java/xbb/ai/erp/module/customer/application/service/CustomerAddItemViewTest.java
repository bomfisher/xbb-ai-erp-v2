package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class CustomerAddItemViewTest {

    @Test
    void should_return_head_list_and_empty_nested_data() {
        CustomerAdminAppServiceImpl service = new CustomerAdminAppServiceImpl();
        SaveItemVO<CustomerSaveItemVO> result = service.addItem(new BaseDTO());

        assertNotNull(result.getHeadList());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getMain());
        assertTrue(result.getData().getContacts().isEmpty());
        assertTrue(result.getData().getBankAccounts().isEmpty());
    }
}
