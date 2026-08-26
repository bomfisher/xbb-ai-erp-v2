package xbb.ai.erp.module.purchase.infrastructure.persistence.convertor;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseOrderItemPO;

class PurchaseOrderItemConvertorTest {

    @Test
    void should_restore_warehouse_id_from_purchase_order_item() {
        PurchaseOrderItemPO po = new PurchaseOrderItemPO();
        po.setWarehouseId(4001L);

        var item = PurchaseOrderItemConvertor.toDomain(po);

        assertEquals(4001L, item.getWarehouseId());
    }
}
