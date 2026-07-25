package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseSaveItemVO;
import xbb.ai.erp.module.product.application.service.impl.WarehouseAdminAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.FakeWarehouseRepository;
import xbb.ai.erp.module.product.domain.model.Warehouse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WarehouseUpdateItemViewTest {

    @Test
    void should_return_existing_data_for_update_item() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setCorpid("corp-001");
        warehouse.setWarehouseCode("WH-001");
        warehouse.setWarehouseName("杭州仓");

        WarehouseAdminAppServiceImpl service = WarehouseAdminAppServiceImpl.forTesting(new FakeWarehouseRepository(List.of(warehouse)));
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");
        dto.setId(1L);

        SaveItemVO<WarehouseSaveItemVO> result = service.updateItem(dto);

        assertNotNull(result.getHeadList());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getMain());
        assertEquals("WH-001", result.getData().getMain().getWarehouseCode());
    }
}
