package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.product.admin.dto.WarehouseListDTO;
import xbb.ai.erp.module.product.admin.vo.WarehouseDetailVO;
import xbb.ai.erp.module.product.admin.vo.WarehouseListItemVO;
import xbb.ai.erp.module.product.application.service.impl.WarehouseAdminAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.FakeWarehouseRepository;
import xbb.ai.erp.module.product.domain.model.Warehouse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class WarehouseQueryServiceTest {

    @Test
    void should_return_warehouse_list_items() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setCorpid("corp-001");
        warehouse.setWarehouseCode("WH-001");
        warehouse.setWarehouseName("杭州仓");
        warehouse.setEnableStatus(1);
        warehouse.setBizStatus("ENABLED");

        WarehouseAdminAppServiceImpl service = WarehouseAdminAppServiceImpl.forTesting(new FakeWarehouseRepository(List.of(warehouse)));
        WarehouseListDTO dto = new WarehouseListDTO();
        dto.setCorpid("corp-001");
        dto.setPageNum(1);
        dto.setPageSize(20);

        ListBaseVO<WarehouseListItemVO> result = service.list(dto);

        assertFalse(result.getList().isEmpty());
        assertEquals("WH-001", result.getList().get(0).getWarehouseCode());
        assertNotNull(result.getHeadList());
    }

    @Test
    void should_return_warehouse_detail_data() {
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setCorpid("corp-001");
        warehouse.setWarehouseCode("WH-001");
        warehouse.setWarehouseName("杭州仓");

        WarehouseAdminAppServiceImpl service = WarehouseAdminAppServiceImpl.forTesting(new FakeWarehouseRepository(List.of(warehouse)));
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");
        dto.setId(1L);

        WarehouseDetailVO result = service.detail(dto);

        assertNotNull(result.getHeadList());
        assertNotNull(result.getMainData());
        assertEquals("WH-001", result.getMainData().getMain().getWarehouseCode());
    }
}
