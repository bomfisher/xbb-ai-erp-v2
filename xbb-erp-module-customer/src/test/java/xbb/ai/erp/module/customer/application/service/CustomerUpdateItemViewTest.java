package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class CustomerUpdateItemViewTest {

    @Test
    void should_return_existing_data_structure_for_update_item() {
        CustomerAdminAppServiceImpl service = new CustomerAdminAppServiceImpl();
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");
        dto.setId(1L);

        SaveItemVO<CustomerSaveItemVO> result = service.updateItem(dto);

        assertNotNull(result.getHeadList());
        assertNotNull(result.getData());
    }
}
