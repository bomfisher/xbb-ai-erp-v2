package xbb.ai.erp.module.demo.application.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

class DefaultDemoFieldFactoryTest {

    private final DefaultDemoFieldFactory factory = new DefaultDemoFieldFactory();

    @Test
    void shouldExposeDemoItemFieldsOnlyForFormScenes() {
        var createField = factory.getFields(SceneTypeEnum.CREATE).stream()
            .filter(field -> "items".equals(field.getAttr())).findFirst().orElseThrow();
        var secondCreateField = factory.getFields(SceneTypeEnum.CREATE).stream()
            .filter(field -> "items2".equals(field.getAttr())).findFirst().orElseThrow();

        assertEquals(1, createField.getSubFields().size());
        assertEquals("name", createField.getSubFields().getFirst().getAttr());
        assertEquals("name", secondCreateField.getSubFields().getFirst().getAttr());
        assertFalse(factory.getFields(SceneTypeEnum.LIST).stream()
            .anyMatch(field -> "items".equals(field.getAttr())));
        assertFalse(factory.getFields(SceneTypeEnum.LIST).stream()
            .anyMatch(field -> "items2".equals(field.getAttr())));
        assertTrue(createField.getSubFields().getFirst().getRequired() == 1);
    }
}
