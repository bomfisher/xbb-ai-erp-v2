package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductBrandSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandSaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductBrandVO;
import xbb.ai.erp.module.product.application.service.impl.ProductBrandAdminAppServiceImpl;
import xbb.ai.erp.module.product.domain.model.ProductBrand;
import xbb.ai.erp.module.product.domain.repository.ProductBrandRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductBrandServiceTest {

    @Test
    void should_return_list_base_vo_for_product_brand_list() {
        ProductBrandRepository repository = new InMemoryProductBrandRepository(List.of(buildBrand()));
        ProductBrandAdminAppServiceImpl service = new ProductBrandAdminAppServiceImpl(repository);

        ProductBrandListDTO dto = new ProductBrandListDTO();
        dto.setCorpid("corp-001");
        dto.setOffset(0);
        dto.setPageSize(20);

        ListBaseVO<ProductBrandVO> result = service.list(dto);

        assertFalse(result.getList().isEmpty());
        assertEquals("BR-001", result.getList().get(0).getBrandCode());
        assertEquals(1, result.getPageHelper().getPage());
        assertEquals(1, result.getPageHelper().getCount());
        assertNotNull(result.getHeadList());
    }

    @Test
    void should_return_brand_add_item_skeleton() {
        ProductBrandAdminAppServiceImpl service = new ProductBrandAdminAppServiceImpl(new InMemoryProductBrandRepository(List.of()));

        SaveItemVO<ProductBrandSaveItemVO> result = service.addItem(new BaseDTO());

        assertNotNull(result.getHeadList());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getMain());
    }

    @Test
    void should_return_existing_brand_for_update_item_and_detail() {
        ProductBrandAdminAppServiceImpl service = new ProductBrandAdminAppServiceImpl(new InMemoryProductBrandRepository(List.of(buildBrand())));
        IdBaseDTO dto = idBaseDTO();

        SaveItemVO<ProductBrandSaveItemVO> updateItem = service.updateItem(dto);
        ProductBrandDetailVO detail = service.detail(dto);

        assertEquals("BR-001", updateItem.getData().getMain().getBrandCode());
        assertNotNull(detail.getHeadList());
        assertEquals("BR-001", detail.getMainData().getMain().getBrandCode());
    }

    @Test
    void should_save_and_delete_brand_with_new_facade_contract() {
        InMemoryProductBrandRepository repository = new InMemoryProductBrandRepository(List.of());
        ProductBrandAdminAppServiceImpl service = new ProductBrandAdminAppServiceImpl(repository);
        ProductBrandSaveDTO saveDTO = new ProductBrandSaveDTO();
        saveDTO.setCorpid("corp-001");
        saveDTO.setUserId("EMP-001");
        ProductBrandMainDTO main = new ProductBrandMainDTO();
        main.setBrandCode("BR-NEW");
        main.setBrandName("新品牌");
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
    void should_reject_missing_brand_id_for_update_item() {
        ProductBrandAdminAppServiceImpl service = new ProductBrandAdminAppServiceImpl(new InMemoryProductBrandRepository(List.of()));
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> service.updateItem(dto));

        assertEquals("id不能为空", exception.getMessage());
    }

    private ProductBrand buildBrand() {
        ProductBrand brand = new ProductBrand();
        brand.setId(1L);
        brand.setCorpid("corp-001");
        brand.setBrandCode("BR-001");
        brand.setBrandName("品牌A");
        brand.setSortNo(1);
        brand.setEnableStatus(1);
        return brand;
    }

    private IdBaseDTO idBaseDTO() {
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");
        dto.setId(1L);
        return dto;
    }

    private static class InMemoryProductBrandRepository implements ProductBrandRepository {
        private final List<ProductBrand> records = new ArrayList<>();

        private InMemoryProductBrandRepository(List<ProductBrand> records) {
            this.records.addAll(records);
        }

        @Override
        public Long save(ProductBrand productBrand, String userId) {
            long id = records.size() + 1L;
            productBrand.setId(id);
            records.add(productBrand);
            return id;
        }

        @Override
        public void update(ProductBrand productBrand, String userId) {
            ProductBrand existed = findById(productBrand.getCorpid(), productBrand.getId());
            if (existed == null) {
                return;
            }
            existed.setBrandCode(productBrand.getBrandCode());
            existed.setBrandName(productBrand.getBrandName());
            existed.setSortNo(productBrand.getSortNo());
            existed.setEnableStatus(productBrand.getEnableStatus());
        }

        @Override
        public void removeById(String corpid, Long id, String userId) {
            records.removeIf(item -> item.getCorpid().equals(corpid) && item.getId().equals(id));
        }

        @Override
        public ProductBrand findById(String corpid, Long id) {
            return records.stream().filter(item -> item.getCorpid().equals(corpid) && item.getId().equals(id)).findFirst().orElse(null);
        }

        @Override
        public List<ProductBrand> findByCondition(Map<String, Object> condition) {
            return List.copyOf(records);
        }

        @Override
        public long count(Map<String, Object> condition) {
            return records.size();
        }
    }
}
