package xbb.ai.erp.module.product.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductCategoryListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductCategoryMainDTO;
import xbb.ai.erp.module.product.admin.dto.ProductCategorySaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategorySaveItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductCategoryVO;
import xbb.ai.erp.module.product.application.service.impl.ProductCategoryAdminAppServiceImpl;
import xbb.ai.erp.module.product.domain.model.ProductCategory;
import xbb.ai.erp.module.product.domain.repository.ProductCategoryRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ProductCategoryServiceTest {

    @Test
    void should_return_list_base_vo_for_product_category_list() {
        ProductCategoryRepository repository = new InMemoryProductCategoryRepository(List.of(buildCategory()));
        ProductCategoryAdminAppServiceImpl service = new ProductCategoryAdminAppServiceImpl(repository);

        ProductCategoryListDTO dto = new ProductCategoryListDTO();
        dto.setCorpid("corp-001");
        dto.setOffset(0);
        dto.setPageSize(20);

        ListBaseVO<ProductCategoryVO> result = service.list(dto);

        assertFalse(result.getList().isEmpty());
        assertEquals("CAT-001", result.getList().get(0).getCategoryCode());
        assertEquals(1, result.getPageHelper().getPage());
        assertEquals(1, result.getPageHelper().getCount());
        assertNotNull(result.getHeadList());
    }

    @Test
    void should_return_category_add_item_skeleton() {
        ProductCategoryAdminAppServiceImpl service = new ProductCategoryAdminAppServiceImpl(new InMemoryProductCategoryRepository(List.of()));

        SaveItemVO<ProductCategorySaveItemVO> result = service.addItem(new BaseDTO());

        assertNotNull(result.getHeadList());
        assertNotNull(result.getData());
        assertNotNull(result.getData().getMain());
    }

    @Test
    void should_return_existing_category_for_update_item_and_detail() {
        ProductCategoryAdminAppServiceImpl service = new ProductCategoryAdminAppServiceImpl(new InMemoryProductCategoryRepository(List.of(buildCategory())));
        IdBaseDTO dto = idBaseDTO();

        SaveItemVO<ProductCategorySaveItemVO> updateItem = service.updateItem(dto);
        ProductCategoryDetailVO detail = service.detail(dto);

        assertEquals("CAT-001", updateItem.getData().getMain().getCategoryCode());
        assertNotNull(detail.getHeadList());
        assertEquals("CAT-001", detail.getMainData().getMain().getCategoryCode());
    }

    @Test
    void should_save_and_delete_category_with_new_facade_contract() {
        InMemoryProductCategoryRepository repository = new InMemoryProductCategoryRepository(List.of());
        ProductCategoryAdminAppServiceImpl service = new ProductCategoryAdminAppServiceImpl(repository);
        ProductCategorySaveDTO saveDTO = new ProductCategorySaveDTO();
        saveDTO.setCorpid("corp-001");
        saveDTO.setUserId("EMP-001");
        ProductCategoryMainDTO main = new ProductCategoryMainDTO();
        main.setCategoryCode("CAT-NEW");
        main.setCategoryName("新分类");
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
    void should_reject_missing_category_id_for_update_item() {
        ProductCategoryAdminAppServiceImpl service = new ProductCategoryAdminAppServiceImpl(new InMemoryProductCategoryRepository(List.of()));
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");

        BizException exception = assertThrows(BizException.class, () -> service.updateItem(dto));

        assertEquals("id不能为空", exception.getMessage());
    }

    private ProductCategory buildCategory() {
        ProductCategory category = new ProductCategory();
        category.setId(1L);
        category.setCorpid("corp-001");
        category.setCategoryCode("CAT-001");
        category.setCategoryName("分类A");
        category.setParentId(0L);
        category.setCategoryLevel(1);
        category.setSortNo(1);
        category.setEnableStatus(1);
        return category;
    }

    private IdBaseDTO idBaseDTO() {
        IdBaseDTO dto = new IdBaseDTO();
        dto.setCorpid("corp-001");
        dto.setId(1L);
        return dto;
    }

    private static class InMemoryProductCategoryRepository implements ProductCategoryRepository {
        private final List<ProductCategory> records = new ArrayList<>();

        private InMemoryProductCategoryRepository(List<ProductCategory> records) {
            this.records.addAll(records);
        }

        @Override
        public Long save(ProductCategory productCategory, String userId) {
            long id = records.size() + 1L;
            productCategory.setId(id);
            records.add(productCategory);
            return id;
        }

        @Override
        public void update(ProductCategory productCategory, String userId) {
            ProductCategory existed = findById(productCategory.getCorpid(), productCategory.getId());
            if (existed == null) {
                return;
            }
            existed.setCategoryCode(productCategory.getCategoryCode());
            existed.setCategoryName(productCategory.getCategoryName());
            existed.setParentId(productCategory.getParentId());
            existed.setCategoryLevel(productCategory.getCategoryLevel());
            existed.setSortNo(productCategory.getSortNo());
            existed.setEnableStatus(productCategory.getEnableStatus());
        }

        @Override
        public void removeById(String corpid, Long id, String userId) {
            records.removeIf(item -> item.getCorpid().equals(corpid) && item.getId().equals(id));
        }

        @Override
        public ProductCategory findById(String corpid, Long id) {
            return records.stream().filter(item -> item.getCorpid().equals(corpid) && item.getId().equals(id)).findFirst().orElse(null);
        }

        @Override
        public List<ProductCategory> findByCondition(Map<String, Object> condition) {
            return List.copyOf(records);
        }

        @Override
        public long count(Map<String, Object> condition) {
            return records.size();
        }
    }
}
