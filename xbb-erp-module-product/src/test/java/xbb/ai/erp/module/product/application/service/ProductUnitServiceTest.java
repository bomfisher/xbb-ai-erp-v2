package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductUnitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductUnitVO;
import xbb.ai.erp.module.product.application.service.impl.ProductUnitAdminAppServiceImpl;
import xbb.ai.erp.module.product.domain.model.ProductUnit;
import xbb.ai.erp.module.product.domain.repository.ProductUnitRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductUnitServiceTest {

    @Test
    void should_return_list_base_vo_for_product_unit_list() {
        ProductUnitRepository repository = new InMemoryProductUnitRepository(List.of(buildUnit()));
        ProductUnitAdminAppServiceImpl service = new ProductUnitAdminAppServiceImpl(repository);

        ProductUnitListDTO dto = new ProductUnitListDTO();
        dto.setCorpid("corp-001");
        dto.setOffset(0);
        dto.setPageSize(20);

        ListBaseVO<ProductUnitVO> result = service.list(dto);

        assertFalse(result.getList().isEmpty());
        assertEquals("UNIT-001", result.getList().get(0).getUnitCode());
        assertEquals(1, result.getPageHelper().getPage());
        assertEquals(1, result.getPageHelper().getCount());
        assertNotNull(result.getHeadList());
    }

    @Test
    void should_return_unit_add_item_skeleton() {
        ProductUnitAdminAppServiceImpl service = new ProductUnitAdminAppServiceImpl(new InMemoryProductUnitRepository(List.of()));

        SaveItemVO<ProductUnitSaveItemVO> result = service.addItem(new BaseDTO());

        assertNotNull(result.getHeadList());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getMain());
    }

    @Test
    void should_return_existing_unit_for_update_item_and_detail() {
        ProductUnitAdminAppServiceImpl service = new ProductUnitAdminAppServiceImpl(new InMemoryProductUnitRepository(List.of(buildUnit())));
        IdBaseDTO dto = idBaseDTO();

        SaveItemVO<ProductUnitSaveItemVO> updateItem = service.updateItem(dto);
        ProductUnitDetailVO detail = service.detail(dto);

        assertEquals("UNIT-001", updateItem.getData().getMain().getUnitCode());
        assertNotNull(detail.getHeadList());
        assertEquals("UNIT-001", detail.getMainData().getMain().getUnitCode());
    }

    @Test
    void should_save_and_delete_unit_with_new_facade_contract() {
        InMemoryProductUnitRepository repository = new InMemoryProductUnitRepository(List.of());
        ProductUnitAdminAppServiceImpl service = new ProductUnitAdminAppServiceImpl(repository);
        ProductUnitSaveDTO saveDTO = new ProductUnitSaveDTO();
        saveDTO.setCorpid("corp-001");
        saveDTO.setUserId("EMP-001");
        ProductUnitMainDTO main = new ProductUnitMainDTO();
        main.setUnitCode("UNIT-NEW");
        main.setUnitName("新单位");
        main.setEnableStatus(1);
        saveDTO.setMain(main);

        Long id = service.save(saveDTO);

        assertEquals(1L, id);

        BatchBaseDTO deleteDTO = new BatchBaseDTO();
        deleteDTO.setCorpid("corp-001");
        deleteDTO.setUserId("EMP-001");
        deleteDTO.setIdList(List.of(id));
        service.delete(deleteDTO);
        assertEquals(0, repository.records.size());
    }

    @Test
    void should_reject_missing_unit_id_for_update_item() {
        ProductUnitAdminAppServiceImpl service = new ProductUnitAdminAppServiceImpl(new InMemoryProductUnitRepository(List.of()));
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> service.updateItem(dto));

        assertEquals("id不能为空", exception.getMessage());
    }

    private ProductUnit buildUnit() {
        ProductUnit unit = new ProductUnit();
        unit.setId(1L);
        unit.setCorpid("corp-001");
        unit.setUnitCode("UNIT-001");
        unit.setUnitName("个");
        unit.setPrecisionNum(2);
        unit.setEnableStatus(1);
        return unit;
    }

    private IdBaseDTO idBaseDTO() {
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");
        dto.setId(1L);
        return dto;
    }

    private static class InMemoryProductUnitRepository implements ProductUnitRepository {
        private final List<ProductUnit> records = new ArrayList<>();

        private InMemoryProductUnitRepository(List<ProductUnit> records) {
            this.records.addAll(records);
        }

        @Override
        public Long save(ProductUnit productUnit, String userId) {
            long id = records.size() + 1L;
            productUnit.setId(id);
            records.add(productUnit);
            return id;
        }

        @Override
        public void update(ProductUnit productUnit, String userId) {
            ProductUnit existed = findById(productUnit.getCorpid(), productUnit.getId());
            if (existed == null) {
                return;
            }
            existed.setUnitCode(productUnit.getUnitCode());
            existed.setUnitName(productUnit.getUnitName());
            existed.setPrecisionNum(productUnit.getPrecisionNum());
            existed.setEnableStatus(productUnit.getEnableStatus());
        }

        @Override
        public void removeById(String corpid, Long id, String userId) {
            records.removeIf(item -> item.getCorpid().equals(corpid) && item.getId().equals(id));
        }

        @Override
        public ProductUnit findById(String corpid, Long id) {
            return records.stream().filter(item -> item.getCorpid().equals(corpid) && item.getId().equals(id)).findFirst().orElse(null);
        }

        @Override
        public List<ProductUnit> findByCondition(Map<String, Object> condition) {
            return List.copyOf(records);
        }

        @Override
        public long count(Map<String, Object> condition) {
            return records.size();
        }
    }
}
