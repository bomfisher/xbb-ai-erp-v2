package xbb.ai.erp.module.demo.application.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.pojo.FilterField;

class DemoListMetaProviderTest {

    private final DemoListMetaProvider provider = new DemoListMetaProvider();

    @Test
    void buildsListMetadataFromDemoFields() {
        List<FilterField> filters = provider.buildFilterMeta(null);

        assertFalse(filters.isEmpty());
        assertEquals("main.comb", filters.stream().filter(item -> "main.comb".equals(item.getAttr())).findFirst().orElseThrow().getAttr());
        assertEquals(2, filters.stream().filter(item -> "main.comb".equals(item.getAttr())).findFirst().orElseThrow().getItemList().size());
        FilterField combMulti = filters.stream().filter(item -> "main.combMulti".equals(item.getAttr())).findFirst().orElseThrow();
        assertEquals("9", combMulti.getFieldType());
        assertEquals("ENUM_MULTI", combMulti.getFilterFieldType());
        assertEquals(2, combMulti.getItemList().size());
        assertTrue(combMulti.getSupportedSymbols().contains("CONTAINS_ALL"));
        assertEquals("ORG_MEMBER", filters.stream().filter(item -> "main.userId".equals(item.getAttr())).findFirst()
            .orElseThrow().getBusinessSelectConfig().getBusinessCode());
        assertFalse(provider.buildFilterConditionMeta(null).isEmpty());
        assertFalse(provider.buildHeaderMeta(null).isEmpty());
    }

    @Test
    void buildsFormCompatibleHeaders() {
        FieldEntity comb = provider.buildHeaderMeta(null).stream()
            .filter(item -> "main.comb".equals(item.getAttr()))
            .findFirst()
            .orElseThrow();
        FieldEntity user = provider.buildHeaderMeta(null).stream()
            .filter(item -> "main.userId".equals(item.getAttr()))
            .findFirst()
            .orElseThrow();

        assertEquals(2, comb.getItemList().size());
        assertEquals(List.of("CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"), provider.buildFilterMeta(null).stream()
            .filter(item -> "main.comb".equals(item.getAttr())).findFirst().orElseThrow().getSupportedSymbols());
        assertNotNull(user.getBusinessSelectConfig());
        assertEquals("ORG_MEMBER", user.getBusinessSelectConfig().getBusinessCode());
        assertNull(user.getBusinessSelectConfig().getQuickSearchUrl());
    }

    @Test
    void buildsEditRowAction() {
        var action = provider.buildRowActionMeta(null).getRowActionList().getFirst();

        assertEquals("EDIT", action.getActionCode());
        assertEquals("编辑", action.getActionName());
        assertEquals(10, action.getSort());
        assertEquals("PRIMARY", action.getShowMode());
        assertEquals("NONE", action.getConfirmType());
    }
}
