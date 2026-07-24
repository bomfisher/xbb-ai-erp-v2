package xbb.ai.erp.scene.meta;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SceneFieldAssemblerTest {

    @Test
    void should_build_field_entity_from_scene_field_meta() {
        SceneFieldMeta meta = new SceneFieldMeta("main.customerName", "客户名称", 1, 1, 1);

        FieldEntity entity = SceneFieldAssembler.build(meta);

        assertEquals("main.customerName", entity.getAttr());
        assertEquals("客户名称", entity.getAttrName());
        assertEquals("1", entity.getFieldType());
        assertEquals(1, entity.getRequired());
        assertEquals(1, entity.getEditable());
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
}
