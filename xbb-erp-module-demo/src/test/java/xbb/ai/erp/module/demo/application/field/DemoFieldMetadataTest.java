package xbb.ai.erp.module.demo.application.field;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.demo.application.assembler.DemoFieldAssembler;
import xbb.ai.erp.module.demo.application.provider.DemoListMetaProvider;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

class DemoFieldMetadataTest {

    private final DefaultDemoFieldFactory fieldFactory = new DefaultDemoFieldFactory();

    @Test
    void shouldBuildSubFieldsForCreateAndUpdateOnly() {
        List<FieldEntity> createFields = DemoFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.CREATE));
        FieldEntity itemField = createFields.stream()
            .filter(field -> "items".equals(field.getAttr()))
            .findFirst()
            .orElseThrow();

        assertNotNull(itemField.getSubField());
        assertEquals(1, itemField.getSubField().size());
        assertEquals("name", itemField.getSubField().get(0).getAttr());
        assertEquals("1", itemField.getSubField().get(0).getFieldType());
        assertTrue(DemoFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.LIST)).stream()
            .noneMatch(field -> "items".equals(field.getAttr())));
    }

    @Test
    void shouldDeriveListFilterRulesFromFieldEnum() {
        DemoListMetaProvider provider = new DemoListMetaProvider(fieldFactory);
        Map<String, xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo> filters = provider.buildFilterConditionMeta(new ListCommonQueryDTO());

        assertEquals("user_id", filters.get("userId").getColumn());
        assertEquals("ID", filters.get("userId").getFieldType());
        assertTrue(filters.get("userId").getSupportedSymbols().contains("IN"));
        assertEquals("ENUM", filters.get("comb").getFieldType());
        assertFalse(filters.containsKey("combMulti"));
        assertFalse(filters.containsKey("address"));
        assertTrue(provider.buildTopButtonMeta(new ListCommonQueryDTO()).getTopButtonList().isEmpty());
        assertTrue(provider.buildRowActionMeta(new ListCommonQueryDTO()).getRowActionList().isEmpty());
    }
}
