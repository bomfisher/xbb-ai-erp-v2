package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.product.admin.dto.WarehouseMainDTO;
import xbb.ai.erp.module.product.admin.dto.WarehouseSaveDTO;
import xbb.ai.erp.module.product.application.service.impl.WarehouseAdminAppServiceImpl;
import xbb.ai.erp.module.product.application.service.support.InMemoryWarehouseRepository;
import xbb.ai.erp.module.product.domain.model.Warehouse;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class WarehouseSaveDeleteServiceTest {

    @Test
    void should_reject_empty_id_list_for_delete() {
        InMemoryWarehouseRepository repository = new InMemoryWarehouseRepository();
        WarehouseAdminAppServiceImpl service = WarehouseAdminAppServiceImpl.forTesting(repository);
        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> service.delete(dto));

        assertEquals("idList不能为空", exception.getMessage());
    }

    @Test
    void should_insert_new_warehouse_on_save() {
        InMemoryWarehouseRepository repository = new InMemoryWarehouseRepository();
        WarehouseAdminAppServiceImpl service = WarehouseAdminAppServiceImpl.forTesting(repository);
        WarehouseSaveDTO dto = new WarehouseSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        WarehouseMainDTO main = new WarehouseMainDTO();
        main.setBizOrgId(10L);
        main.setWarehouseCode("WH-001");
        main.setWarehouseName("杭州仓");
        main.setWarehouseType("FINISHED");
        main.setEnableStatus(1);
        main.setBizStatus("ENABLED");
        dto.setMain(main);

        Long warehouseId = service.save(dto);

        assertEquals(1, repository.all().size());
        assertEquals(warehouseId, repository.all().get(0).getId());
        assertEquals("WH-001", repository.all().get(0).getWarehouseCode());
        assertEquals("user-001", repository.all().get(0).getCreatorId());
    }

    @Test
    void should_update_existing_warehouse_on_save() {
        InMemoryWarehouseRepository repository = new InMemoryWarehouseRepository();
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setCorpid("corp-001");
        warehouse.setWarehouseCode("WH-001");
        warehouse.setWarehouseName("杭州仓");
        repository.seed(warehouse);
        WarehouseAdminAppServiceImpl service = WarehouseAdminAppServiceImpl.forTesting(repository);

        WarehouseSaveDTO dto = new WarehouseSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-002");
        WarehouseMainDTO main = new WarehouseMainDTO();
        main.setId(1L);
        main.setWarehouseCode("WH-001");
        main.setWarehouseName("杭州总仓");
        main.setBizStatus("ENABLED");
        dto.setMain(main);

        Long warehouseId = service.save(dto);

        assertEquals(1L, warehouseId);
        assertEquals(1, repository.all().size());
        assertEquals("杭州总仓", repository.all().get(0).getWarehouseName());
        assertEquals("user-002", repository.all().get(0).getModifyId());
    }

    @Test
    void should_remove_warehouses_by_batch_ids() {
        InMemoryWarehouseRepository repository = new InMemoryWarehouseRepository();
        Warehouse warehouse = new Warehouse();
        warehouse.setId(1L);
        warehouse.setCorpid("corp-001");
        warehouse.setWarehouseCode("WH-001");
        repository.seed(warehouse);
        WarehouseAdminAppServiceImpl service = WarehouseAdminAppServiceImpl.forTesting(repository);
        BatchBaseDTO dto = new BatchBaseDTO();
        dto.setCorpid("corp-001");
        dto.setIdList(List.of(1L));

        service.delete(dto);

        assertEquals(0, repository.all().size());
    }
}
