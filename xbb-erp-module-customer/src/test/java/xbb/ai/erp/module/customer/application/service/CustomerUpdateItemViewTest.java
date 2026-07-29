package xbb.ai.erp.module.customer.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.customer.admin.vo.CustomerSaveItemVO;
import xbb.ai.erp.module.customer.application.service.impl.CustomerAdminAppServiceImpl;

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
}
