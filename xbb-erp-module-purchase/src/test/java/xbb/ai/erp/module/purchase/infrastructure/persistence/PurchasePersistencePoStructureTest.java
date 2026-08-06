package xbb.ai.erp.module.purchase.infrastructure.persistence;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseOrderItemPO;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseOrderPO;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchasePendingTaskPO;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseRequestItemPO;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseRequestPO;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseSourceRelationPO;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PurchasePersistencePoStructureTest {

    @Test
    void should_not_use_boolean_fields_in_purchase_persistent_po_classes() {
        List<Class<?>> poClasses = List.of(
            PurchaseRequestPO.class,
            PurchaseRequestItemPO.class,
            PurchaseOrderPO.class,
            PurchaseOrderItemPO.class,
            PurchasePendingTaskPO.class,
            PurchaseSourceRelationPO.class
        );

        for (Class<?> poClass : poClasses) {
            assertTrue(BaseEntity.class.isAssignableFrom(poClass), () -> poClass.getSimpleName() + " 必须继承 BaseEntity");
            for (Field field : poClass.getDeclaredFields()) {
                Class<?> fieldType = field.getType();
                assertFalse(
                    fieldType == Boolean.class || fieldType == boolean.class,
                    () -> poClass.getSimpleName() + "." + field.getName() + " 不允许使用 Boolean/boolean 对接数据库"
                );
            }
        }
    }
}
