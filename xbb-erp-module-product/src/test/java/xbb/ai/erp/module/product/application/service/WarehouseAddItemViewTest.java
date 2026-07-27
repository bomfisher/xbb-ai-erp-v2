package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseSaveItemVO;
import xbb.ai.erp.module.product.application.service.impl.WarehouseAdminAppServiceImpl;

import static org.junit.jupiter.api.Assertions.assertNotNull;

class WarehouseAddItemViewTest {

    @Test
    void should_return_head_list_and_empty_main_data() {
        WarehouseAdminAppServiceImpl service = WarehouseAdminAppServiceImpl.forTesting(null);

        SaveItemVO<WarehouseSaveItemVO> result = service.addItem(new BaseDTO());

        assertNotNull(result.getHeadList());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getMain());
    }
}
