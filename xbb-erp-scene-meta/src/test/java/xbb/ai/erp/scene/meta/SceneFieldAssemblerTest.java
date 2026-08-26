package xbb.ai.erp.scene.meta;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.filed.ProductSelectSourceModeEnum;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class SceneFieldAssemblerTest {

    @Test
    void should_build_field_entity_from_scene_field_meta() {
        FieldItem item = new FieldItem();
        item.setValue("1");
        item.setText("启用");
        SceneFieldMeta meta = new SceneFieldMeta("main.customerName", "客户名称", 1, 1, 1, List.of(item));

        FieldEntity entity = SceneFieldAssembler.build(meta);

        assertEquals("main.customerName", entity.getAttr());
        assertEquals("客户名称", entity.getAttrName());
        assertEquals("1", entity.getFieldType());
        assertEquals(1, entity.getRequired());
        assertEquals(1, entity.getEditable());
        assertEquals(1, entity.getItemList().size());
        assertEquals("1", entity.getItemList().get(0).getValue());
    }

    @Test
    void should_build_head_list_from_scene_field_meta_list() {
        List<SceneFieldMeta> fields = List.of(
            new SceneFieldMeta("main.customerName", "客户名称", 1, 1, 1),
            new SceneFieldMeta("contacts.contactName", "联系人姓名", 1, 0, 1)
        );

        List<FieldEntity> headList = SceneFieldAssembler.buildHeadList(fields);

        assertEquals(2, headList.size());
        assertEquals("main.customerName", headList.get(0).getAttr());
        assertEquals("contacts.contactName", headList.get(1).getAttr());
    }

    @Test
    void should_build_product_select_config_with_source_mode() {
        SceneFieldMeta meta = new SceneFieldMeta(
            "skuId", "产品", FieldTypeEnum.PRODUCT.getType(), 1, 1, List.of(), "PRODUCT_SKU", List.of(),
            ProductSelectSourceModeEnum.UPSTREAM_ONLY);

        FieldEntity entity = SceneFieldAssembler.build(meta);

        assertEquals("product-sku", entity.getProductSelectConfig().getProductType());
        assertEquals("PRODUCT_SKU", entity.getProductSelectConfig().getBusinessCode());
        assertEquals("UPSTREAM_ONLY", entity.getProductSelectConfig().getSourceMode());
        assertEquals("UPSTREAM_DOCUMENT", entity.getProductSelectConfig().getDefaultSource());
    }
}
