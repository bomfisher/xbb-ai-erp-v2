package xbb.ai.erp.module.purchase.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

class PurchaseOrderFieldEnumTest {

    @Test
    void shouldExposePurchaseProductSubFieldsOnlyForFormScenes() {
        Map<String, PurchaseOrderFieldEnum> fields = java.util.Arrays.stream(PurchaseOrderFieldEnum.values())
            .collect(Collectors.toMap(PurchaseOrderFieldEnum::getAttr, field -> field));

        PurchaseOrderFieldEnum items = fields.get("items");
        assertEquals(FieldTypeEnum.SUB_ITEM, items.getFieldType());
        assertTrue(items.supports(SceneTypeEnum.CREATE));
        assertTrue(items.supports(SceneTypeEnum.UPDATE));
        assertFalse(items.supports(SceneTypeEnum.LIST));
        assertEquals("skuId", items.getSubFields().getFirst().getAttr());
        assertEquals(FieldTypeEnum.PRODUCT.getType(), items.getSubFields().getFirst().getFieldType());
        assertEquals("PRODUCT_SKU", items.getSubFields().getFirst().getBusinessCode());
        assertEquals("WAREHOUSE", items.getSubFields().get(1).getBusinessCode());
        assertEquals(FieldTypeEnum.STOCK.getType(), items.getSubFields().get(2).getFieldType());
        assertEquals(0, items.getSubFields().get(2).getEditable());
    }

    @Test
    void shouldAssembleProductAndWarehouseSelectionConfigs() {
        Map<String, FieldEntity> fields = SceneFieldAssembler.buildHeadList(
                new xbb.ai.erp.module.purchase.application.field.PurchaseOrderFieldFactory()
                    .getFields(SceneTypeEnum.CREATE))
            .stream()
            .collect(Collectors.toMap(FieldEntity::getAttr, field -> field));

        FieldEntity items = fields.get("items");
        FieldEntity product = items.getSubField().getFirst();
        assertEquals("50", product.getFieldType());
        assertEquals("product-sku", product.getProductSelectConfig().getProductType());
        assertEquals("PRODUCT_SKU", product.getProductSelectConfig().getBusinessCode());
        assertEquals(null, product.getBusinessSelectConfig());
        assertEquals("WAREHOUSE", items.getSubField().get(1).getBusinessSelectConfig().getBusinessCode());
    }
}
