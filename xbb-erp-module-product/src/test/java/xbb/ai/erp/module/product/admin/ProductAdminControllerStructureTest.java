package xbb.ai.erp.module.product.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftLoadDTO;
import xbb.ai.erp.module.product.admin.dto.ProductDraftSaveDTO;
import xbb.ai.erp.module.product.admin.dto.ProductListDTO;
import xbb.ai.erp.module.product.admin.dto.ProductSubmitSaveDTO;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class ProductAdminControllerStructureTest {

    @Test
    void should_declare_product_endpoints_with_draft_flow() throws Exception {
        Method list = ProductAdminController.class.getMethod("list", ProductListDTO.class);
        Method addItem = ProductAdminController.class.getMethod("addItem", BaseDTO.class);
        Method updateItem = ProductAdminController.class.getMethod("updateItem", IdBaseDTO.class);
        Method saveDraft = ProductAdminController.class.getMethod("saveDraft", ProductDraftSaveDTO.class);
        Method saveAndSubmit = ProductAdminController.class.getMethod("saveAndSubmit", ProductSubmitSaveDTO.class);
        Method draftList = ProductAdminController.class.getMethod("draftList", ProductDraftListDTO.class);
        Method loadDraft = ProductAdminController.class.getMethod("loadDraft", ProductDraftLoadDTO.class);
        Method detail = ProductAdminController.class.getMethod("detail", IdBaseDTO.class);
        Method delete = ProductAdminController.class.getMethod("delete", BatchBaseDTO.class);

        assertNotNull(list);
        assertNotNull(addItem);
        assertNotNull(updateItem);
        assertNotNull(saveDraft);
        assertNotNull(saveAndSubmit);
        assertNotNull(draftList);
        assertNotNull(loadDraft);
        assertNotNull(detail);
        assertNotNull(delete);
    }

    @Test
    void should_depend_on_product_admin_app_service() throws Exception {
        Field field = ProductAdminController.class.getDeclaredField("productAdminAppService");
        assertEquals(
            Class.forName("xbb.ai.erp.module.product.application.service.ProductAdminAppService"),
            field.getType()
        );
    }

    @Test
    void should_make_delete_endpoint_return_base_vo_wrapper() throws Exception {
        Method delete = ProductAdminController.class.getMethod("delete", BatchBaseDTO.class);
        Type genericReturnType = delete.getGenericReturnType();
        ParameterizedType resultType = (ParameterizedType) genericReturnType;

        assertEquals("xbb.ai.erp.base.common.vo.ResultVO", delete.getReturnType().getName());
        assertEquals(BaseVO.class, resultType.getActualTypeArguments()[0]);
    }
}
