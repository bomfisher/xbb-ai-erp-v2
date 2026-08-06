package xbb.ai.erp.module.supplier.admin;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PostMapping;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierBusinessSelectQueryDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierBusinessSelectOptionVO;
import xbb.ai.erp.module.supplier.application.service.SupplierAdminAppService;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierBusinessSelectControllerTest {

    @Test
    void should_expose_supplier_business_select_endpoints() throws Exception {
        Method quickSearch = SupplierAdminController.class.getDeclaredMethod("businessSelectQuickSearch", SupplierBusinessSelectQueryDTO.class);
        Method dialogSearch = SupplierAdminController.class.getDeclaredMethod("businessSelectDialogSearch", SupplierBusinessSelectQueryDTO.class);
        Method getById = SupplierAdminController.class.getDeclaredMethod("businessSelectGetById", SupplierBusinessSelectQueryDTO.class);

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
    void should_wrap_supplier_business_select_results() {
        SupplierAdminAppService appService = new SupplierAdminAppService() {
            @Override
            public xbb.ai.erp.base.common.vo.ListBaseVO<xbb.ai.erp.module.supplier.admin.vo.SupplierListItemVO> list(xbb.ai.erp.module.supplier.admin.dto.SupplierListDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public xbb.ai.erp.base.common.vo.SaveItemVO<xbb.ai.erp.module.supplier.admin.vo.SupplierSaveItemVO> addItem(xbb.ai.erp.base.common.dto.BaseDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public xbb.ai.erp.base.common.vo.SaveItemVO<xbb.ai.erp.module.supplier.admin.vo.SupplierSaveItemVO> updateItem(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Long save(xbb.ai.erp.module.supplier.admin.dto.SupplierSaveDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public xbb.ai.erp.module.supplier.admin.vo.SupplierDetailVO detail(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void delete(xbb.ai.erp.base.common.dto.BatchBaseDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<SupplierBusinessSelectOptionVO> businessSelectQuickSearch(SupplierBusinessSelectQueryDTO dto) {
                SupplierBusinessSelectOptionVO option = new SupplierBusinessSelectOptionVO();
                option.setId(1L);
                option.setCode("SUP-001");
                option.setName("杭州供应商");
                option.setLabel("SUP-001 杭州供应商");
                return List.of(option);
            }

            @Override
            public ListBaseVO<SupplierBusinessSelectOptionVO> businessSelectDialogSearch(SupplierBusinessSelectQueryDTO dto) {
                ListBaseVO<SupplierBusinessSelectOptionVO> result = new ListBaseVO<>();
                result.setList(businessSelectQuickSearch(dto));
                result.setPageHelper(new ListBaseVO.PageHelper(1, 1));
                return result;
            }

            @Override
            public SupplierBusinessSelectOptionVO businessSelectGetById(SupplierBusinessSelectQueryDTO dto) {
                return businessSelectQuickSearch(dto).get(0);
            }
        };

        SupplierAdminController controller = new SupplierAdminController(appService);
        SupplierBusinessSelectQueryDTO dto = new SupplierBusinessSelectQueryDTO();
        dto.setCorpid("demo-corp");

        ResultVO<List<SupplierBusinessSelectOptionVO>> quickSearch = controller.businessSelectQuickSearch(dto);
        ResultVO<ListBaseVO<SupplierBusinessSelectOptionVO>> dialogSearch = controller.businessSelectDialogSearch(dto);
        ResultVO<SupplierBusinessSelectOptionVO> getById = controller.businessSelectGetById(dto);

        assertTrue(quickSearch.getSuccess());
        assertEquals(1, quickSearch.getData().size());
        assertEquals("SUP-001 杭州供应商", dialogSearch.getData().getList().get(0).getLabel());
        assertEquals("SUP-001", getById.getData().getCode());
    }
}
