package xbb.ai.erp.module.purchase.application.service;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationListDTO;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PurchaseQueryModuleCustomerStyleAppServiceTest {

    @Test
    void should_use_list_base_dto_for_purchase_pending_task_list() {
        assertEquals("ListBaseDTO", PurchasePendingTaskListDTO.class.getSuperclass().getSimpleName());
    }

    @Test
    void should_use_list_base_dto_for_purchase_source_relation_list() {
        assertEquals("ListBaseDTO", PurchaseSourceRelationListDTO.class.getSuperclass().getSimpleName());
    }

    @Test
    void should_use_list_base_dto_for_purchase_request_item_list() {
        assertEquals("ListBaseDTO", PurchaseRequestItemListDTO.class.getSuperclass().getSimpleName());
    }

    @Test
    void should_use_list_base_dto_for_purchase_order_item_list() {
        assertEquals("ListBaseDTO", PurchaseOrderItemListDTO.class.getSuperclass().getSimpleName());
    }
}
