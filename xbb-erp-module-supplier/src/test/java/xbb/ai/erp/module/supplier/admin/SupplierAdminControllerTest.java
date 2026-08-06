package xbb.ai.erp.module.supplier.admin;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierBusinessSelectQueryDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierListDTO;
import xbb.ai.erp.module.supplier.admin.dto.SupplierSaveDTO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierBusinessSelectOptionVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierDetailVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierListItemVO;
import xbb.ai.erp.module.supplier.admin.vo.SupplierSaveItemVO;
import xbb.ai.erp.module.supplier.application.service.SupplierAdminAppService;

import java.lang.reflect.Method;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SupplierAdminControllerTest {

    @Test
    void should_expose_save_endpoint_under_supplier_route() throws Exception {
        RequestMapping mapping = SupplierAdminController.class.getAnnotation(RequestMapping.class);
        assertEquals("/erp/v1/supplier", mapping.value()[0]);

        Method save = SupplierAdminController.class.getDeclaredMethod("save", SupplierSaveDTO.class);
        PostMapping postMapping = save.getAnnotation(PostMapping.class);

        assertNotNull(postMapping);
        assertEquals("/save", postMapping.value()[0]);
        assertTrue(save.getReturnType().equals(ResultVO.class));
    }

    @Test
    void should_wrap_save_result_with_result_vo() {
        final SupplierSaveDTO[] received = new SupplierSaveDTO[1];
        SupplierAdminAppService appService = new SupplierAdminAppService() {
            @Override
            public ListBaseVO<SupplierListItemVO> list(SupplierListDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SaveItemVO<SupplierSaveItemVO> addItem(BaseDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SaveItemVO<SupplierSaveItemVO> updateItem(IdBaseDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public List<SupplierBusinessSelectOptionVO> businessSelectQuickSearch(SupplierBusinessSelectQueryDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public ListBaseVO<SupplierBusinessSelectOptionVO> businessSelectDialogSearch(SupplierBusinessSelectQueryDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public SupplierBusinessSelectOptionVO businessSelectGetById(SupplierBusinessSelectQueryDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public Long save(SupplierSaveDTO dto) {
                received[0] = dto;
                return 99L;
            }

            @Override
            public SupplierDetailVO detail(IdBaseDTO dto) {
                throw new UnsupportedOperationException();
            }

            @Override
            public void delete(BatchBaseDTO dto) {
                throw new UnsupportedOperationException();
            }
        };
        SupplierAdminController controller = new SupplierAdminController(appService);
        SupplierSaveDTO dto = new SupplierSaveDTO();

        ResultVO<Long> result = controller.save(dto);

        assertTrue(result.getSuccess());
        assertEquals(0, result.getCode());
        assertEquals("success", result.getMessage());
        assertEquals(99L, result.getData());
        assertEquals(dto, received[0]);
    }
}
