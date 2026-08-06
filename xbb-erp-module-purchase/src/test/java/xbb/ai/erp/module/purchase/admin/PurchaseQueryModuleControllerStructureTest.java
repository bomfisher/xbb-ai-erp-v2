package xbb.ai.erp.module.purchase.admin;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ResultVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationListDTO;

import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PurchaseQueryModuleControllerStructureTest {

    @Test
    void should_use_list_base_dto_for_purchase_pending_task_list() {
        assertEquals("ListBaseDTO", PurchasePendingTaskListDTO.class.getSuperclass().getSimpleName());
    }

    @Test
    void should_use_list_base_dto_for_purchase_source_relation_list() {
        assertEquals("ListBaseDTO", PurchaseSourceRelationListDTO.class.getSuperclass().getSimpleName());
    }

    @Test
    void should_keep_purchase_pending_task_detail_wrapped_with_result_vo() throws Exception {
        Method detail = PurchasePendingTaskAdminController.class.getMethod("detail", IdBaseDTO.class);

        assertEquals(ResultVO.class, detail.getReturnType());
        ParameterizedType returnType = (ParameterizedType) detail.getGenericReturnType();
        assertEquals("PurchasePendingTaskDetailVO", ((Class<?>) returnType.getActualTypeArguments()[0]).getSimpleName());
    }

    @Test
    void should_keep_purchase_source_relation_detail_wrapped_with_result_vo() throws Exception {
        Method detail = PurchaseSourceRelationAdminController.class.getMethod("detail", IdBaseDTO.class);

        assertEquals(ResultVO.class, detail.getReturnType());
        ParameterizedType returnType = (ParameterizedType) detail.getGenericReturnType();
        assertEquals("PurchaseSourceRelationDetailVO", ((Class<?>) returnType.getActualTypeArguments()[0]).getSimpleName());
    }

    @Test
    void should_keep_purchase_request_item_detail_wrapped_with_result_vo() throws Exception {
        Method detail = PurchaseRequestItemAdminController.class.getMethod("detail", IdBaseDTO.class);

        assertEquals(ResultVO.class, detail.getReturnType());
        ParameterizedType returnType = (ParameterizedType) detail.getGenericReturnType();
        assertEquals("PurchaseRequestItemDetailVO", ((Class<?>) returnType.getActualTypeArguments()[0]).getSimpleName());
    }
}
