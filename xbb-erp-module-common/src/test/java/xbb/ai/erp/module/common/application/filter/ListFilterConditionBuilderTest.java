package xbb.ai.erp.module.common.application.filter;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.pojo.ListFilterCondition;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ListFilterConditionBuilderTest {

    @Test
    void should_map_customer_name_attr_to_safe_column() throws BizException {
        ListFilterCondition condition = buildCondition("customerName", "TEXT", "CONTAINS", List.of("杭州"));

        List<ListFilterCondition> result = new ListFilterConditionBuilder().build(List.of(condition), buildMetaMap());

        assertEquals(1, result.size());
        assertEquals("customer_name", result.get(0).getAttr());
        assertEquals("TEXT", result.get(0).getFieldType());
        assertEquals("CONTAINS", result.get(0).getSymbol());
        assertEquals(List.of("杭州"), result.get(0).getValue());
    }

    @Test
    void should_throw_biz_exception_when_attr_is_invalid() {
        ListFilterCondition condition = buildCondition("unknownAttr", "TEXT", "CONTAINS", List.of("杭州"));

        assertThrows(BizException.class, () -> new ListFilterConditionBuilder().build(List.of(condition), buildMetaMap()));
    }

    @Test
    void should_throw_biz_exception_when_field_type_is_invalid() {
        ListFilterCondition condition = buildCondition("customerName", "ENUM", "CONTAINS", List.of("杭州"));

        assertThrows(BizException.class, () -> new ListFilterConditionBuilder().build(List.of(condition), buildMetaMap()));
    }

    @Test
    void should_throw_biz_exception_when_symbol_is_invalid() {
        ListFilterCondition condition = buildCondition("customerName", "TEXT", "IN", List.of("杭州"));

        assertThrows(BizException.class, () -> new ListFilterConditionBuilder().build(List.of(condition), buildMetaMap()));
    }

    @Test
    void should_throw_biz_exception_when_value_length_is_invalid() {
        ListFilterCondition condition = buildCondition("createTime", "DATE", "BETWEEN", List.of("2026-01-01 00:00:00"));

        assertThrows(BizException.class, () -> new ListFilterConditionBuilder().build(List.of(condition), buildMetaMap()));
    }

    private static Map<String, ListFilterMetaPojo> buildMetaMap() {
        return Map.of(
            "customerName", new ListFilterMetaPojo("customerName", "customer_name", ListFilterFieldTypeEnum.TEXT.name(), Set.of(
                ListFilterSymbolEnum.EQ.name(),
                ListFilterSymbolEnum.NE.name(),
                ListFilterSymbolEnum.CONTAINS.name(),
                ListFilterSymbolEnum.NOT_CONTAINS.name(),
                ListFilterSymbolEnum.IS_EMPTY.name(),
                ListFilterSymbolEnum.IS_NOT_EMPTY.name()
            )),
            "createTime", new ListFilterMetaPojo("createTime", "add_time", ListFilterFieldTypeEnum.DATE.name(), Set.of(
                ListFilterSymbolEnum.EQ.name(),
                ListFilterSymbolEnum.GE.name(),
                ListFilterSymbolEnum.LE.name(),
                ListFilterSymbolEnum.BETWEEN.name(),
                ListFilterSymbolEnum.IS_EMPTY.name(),
                ListFilterSymbolEnum.IS_NOT_EMPTY.name()
            ))
        );
    }

    private static ListFilterCondition buildCondition(String attr, String fieldType, String symbol, List<String> value) {
        ListFilterCondition condition = new ListFilterCondition();
        condition.setAttr(attr);
        condition.setFieldType(fieldType);
        condition.setSymbol(symbol);
        condition.setValue(value);
        return condition;
    }
}
