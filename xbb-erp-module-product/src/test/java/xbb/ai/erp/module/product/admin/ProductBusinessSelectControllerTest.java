package xbb.ai.erp.module.product.admin;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PostMapping;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.product.admin.dto.ProductBusinessSelectQueryDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftLoadDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;
import xbb.ai.erp.module.product.admin.vo.ProductBusinessSelectOptionVO;
import xbb.ai.erp.module.product.admin.vo.ProductDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftDetailVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductDraftSaveVO;
import xbb.ai.erp.module.product.admin.vo.ProductListItemVO;
import xbb.ai.erp.module.product.admin.vo.ProductSaveItemVO;
import xbb.ai.erp.module.product.application.service.ProductAdminAppService;

import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ProductBusinessSelectControllerTest {

    @Test
    void should_expose_product_business_select_endpoints() throws Exception {
        Method quickSearch = ProductAdminController.class.getDeclaredMethod("businessSelectQuickSearch", ProductBusinessSelectQueryDTO.class);
        Method dialogSearch = ProductAdminController.class.getDeclaredMethod("businessSelectDialogSearch", ProductBusinessSelectQueryDTO.class);
        Method getById = ProductAdminController.class.getDeclaredMethod("businessSelectGetById", ProductBusinessSelectQueryDTO.class);

        PostMapping quickSearchMapping = quickSearch.getAnnotation(PostMapping.class);
        PostMapping dialogSearchMapping = dialogSearch.getAnnotation(PostMapping.class);
        PostMapping getByIdMapping = getById.getAnnotation(PostMapping.class);

        assertNotNull(quickSearchMapping);
        assertNotNull(dialogSearchMapping);
        assertNotNull(getByIdMapping);
        assertEquals("/businessSelect/quickSearch", quickSearchMapping.value()[0]);
        assertEquals("/businessSelect/dialogSearch", dialogSearchMapping.value()[0]);
        assertEquals("/businessSelect/getById", getByIdMapping.value()[0]);
    }

    @Test
    void should_wrap_product_business_select_results() {
        ProductAdminAppService appService = new ProductAdminAppService() {
            @Override
            public ListBaseVO<ProductListItemVO> list(ProductListDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SaveItemVO<ProductSaveItemVO> addItem(BaseDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SaveItemVO<ProductSaveItemVO> updateItem(IdBaseDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public ProductDraftSaveVO saveDraft(ProductDraftSaveDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public BaseVO saveAndSubmit(ProductSubmitSaveDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<ProductDraftListItemVO> draftList(ProductDraftListDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public ProductDraftDetailVO loadDraft(ProductDraftLoadDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public ProductDetailVO detail(IdBaseDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void delete(BatchBaseDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<ProductBusinessSelectOptionVO> businessSelectQuickSearch(ProductBusinessSelectQueryDTO dto) {
                ProductBusinessSelectOptionVO option = new ProductBusinessSelectOptionVO();
                option.setId(1001L);
                option.setCode("SKU-001");
                option.setName("红色款");
                option.setLabel("SKU-001 红色款");
                option.setLinePatch(Map.of("skuId", 1001L));
                return List.of(option);
            }

            @Override
            public ListBaseVO<ProductBusinessSelectOptionVO> businessSelectDialogSearch(ProductBusinessSelectQueryDTO dto) {
                ListBaseVO<ProductBusinessSelectOptionVO> result = new ListBaseVO<>();
                result.setList(businessSelectQuickSearch(dto));
                result.setPageHelper(new ListBaseVO.PageHelper(1, 1));
                return result;
            }

            @Override
            public ProductBusinessSelectOptionVO businessSelectGetById(ProductBusinessSelectQueryDTO dto) {
                return businessSelectQuickSearch(dto).get(0);
            }
        };

        ProductAdminController controller = new ProductAdminController(appService);
        ProductBusinessSelectQueryDTO dto = new ProductBusinessSelectQueryDTO();
        dto.setCorpid("demo-corp");

        ResultVO<List<ProductBusinessSelectOptionVO>> quickSearch = controller.businessSelectQuickSearch(dto);
        ResultVO<ListBaseVO<ProductBusinessSelectOptionVO>> dialogSearch = controller.businessSelectDialogSearch(dto);
        ResultVO<ProductBusinessSelectOptionVO> getById = controller.businessSelectGetById(dto);

        assertTrue(quickSearch.getSuccess());
        assertEquals(1, quickSearch.getData().size());
        assertEquals("SKU-001 红色款", dialogSearch.getData().getList().get(0).getLabel());
        assertEquals("SKU-001", getById.getData().getCode());
    }
}
