package xbb.ai.erp.module.purchase.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftLoadDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSubmitSaveDTO;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class PurchaseOrderAdminControllerStructureTest {

    @Test
    void should_expose_customer_style_purchase_order_endpoints() throws Exception {
        Method saveDraft = PurchaseOrderAdminController.class.getMethod("saveDraft", PurchaseOrderDraftSaveDTO.class);
        Method saveAndSubmit = PurchaseOrderAdminController.class.getMethod("saveAndSubmit", PurchaseOrderSubmitSaveDTO.class);
        Method draftList = PurchaseOrderAdminController.class.getMethod("draftList", PurchaseOrderDraftListDTO.class);
        Method loadDraft = PurchaseOrderAdminController.class.getMethod("loadDraft", PurchaseOrderDraftLoadDTO.class);

        assertNotNull(saveDraft);
        assertNotNull(saveAndSubmit);
        assertNotNull(draftList);
        assertNotNull(loadDraft);
    }

    @Test
    void should_keep_delete_as_result_void_like_customer_current_state() throws Exception {
        Method delete = PurchaseOrderAdminController.class.getMethod("delete", BatchBaseDTO.class);

        assertEquals(ResultVO.class, delete.getReturnType());
        ParameterizedType returnType = (ParameterizedType) delete.getGenericReturnType();
        assertEquals(Void.class, returnType.getActualTypeArguments()[0]);
    }

    @Test
    void should_return_base_vo_for_save_and_submit() throws Exception {
        Method saveAndSubmit = PurchaseOrderAdminController.class.getMethod("saveAndSubmit", PurchaseOrderSubmitSaveDTO.class);

        assertEquals(ResultVO.class, saveAndSubmit.getReturnType());
        ParameterizedType returnType = (ParameterizedType) saveAndSubmit.getGenericReturnType();
        assertEquals(BaseVO.class, returnType.getActualTypeArguments()[0]);
    }

    @Test
    void should_return_draft_list_as_result_wrapped_list() throws Exception {
        Method draftList = PurchaseOrderAdminController.class.getMethod("draftList", PurchaseOrderDraftListDTO.class);

        assertEquals(ResultVO.class, draftList.getReturnType());
        ParameterizedType outerType = (ParameterizedType) draftList.getGenericReturnType();
        Type innerType = outerType.getActualTypeArguments()[0];
        assertEquals(List.class, ((ParameterizedType) innerType).getRawType());
    }
}
