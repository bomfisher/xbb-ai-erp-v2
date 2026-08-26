package xbb.ai.erp.module.purchase.admin;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

class PurchaseInboundFieldEnumTest {
    @Test
    void shouldExposeInboundProductSubItemsForFormScenes() {
        PurchaseInboundFieldEnum field = PurchaseInboundFieldEnum.ITEMS;
        assertEquals(FieldTypeEnum.SUB_ITEM, field.getFieldType());
        assertTrue(field.supports(SceneTypeEnum.CREATE));
        assertTrue(field.supports(SceneTypeEnum.UPDATE));
        assertFalse(field.supports(SceneTypeEnum.LIST));
        assertEquals("skuId", field.getSubFields().getFirst().getAttr());
        assertEquals(FieldTypeEnum.PRODUCT.getType(), field.getSubFields().getFirst().getFieldType());

        Map<String, FieldEntity> fields = SceneFieldAssembler.buildHeadList(
            java.util.Arrays.stream(PurchaseInboundFieldEnum.values())
                .filter(item -> item.supports(SceneTypeEnum.CREATE))
                .map(PurchaseInboundFieldEnum::toSceneFieldMeta)
                .toList()).stream().collect(Collectors.toMap(FieldEntity::getAttr, item -> item));
        FieldEntity itemProduct = fields.get("items").getSubField().getFirst();
        assertEquals("product-sku", itemProduct.getProductSelectConfig().getProductType());
        assertEquals(BusinessCodeEnum.PRODUCT_SKU.getCode(), itemProduct.getProductSelectConfig().getBusinessCode());
    }
}
