package xbb.ai.erp.module.purchase.admin;

import org.junit.jupiter.api.Test;
import org.springframework.web.bind.annotation.RequestMapping;

import static org.junit.jupiter.api.Assertions.assertEquals;

class PurchaseControllerRouteStructureTest {

    @Test
    void should_use_distinct_resource_prefixes_for_purchase_controllers() {
        assertRequestMapping(PurchasePendingTaskAdminController.class, "/erp/v1/purchase/pending-task");
        assertRequestMapping(PurchaseRequestAdminController.class, "/erp/v1/purchase/request");
        assertRequestMapping(PurchaseRequestItemAdminController.class, "/erp/v1/purchase/request-item");
        assertRequestMapping(PurchaseOrderAdminController.class, "/erp/v1/purchase/order");
        assertRequestMapping(PurchaseOrderItemAdminController.class, "/erp/v1/purchase/order-item");
        assertRequestMapping(PurchaseSourceRelationAdminController.class, "/erp/v1/purchase/source-relation");
    }

    private void assertRequestMapping(Class<?> controllerClass, String expectedPath) {
        RequestMapping requestMapping = controllerClass.getAnnotation(RequestMapping.class);
        assertEquals(expectedPath, requestMapping.value()[0]);
    }
}
