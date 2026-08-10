package xbb.ai.erp.module.common.application.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;

class ListFilterFieldTypeRuleTest {

  @Test
  void shouldUseBusinessProtocolAndUserSymbolsForBusinessFields() {
    ListFilterFieldTypeRule userRule =
        ListFilterFieldTypeRule.find(FieldTypeEnum.USER.getType()).orElseThrow();
    ListFilterFieldTypeRule businessRule =
        ListFilterFieldTypeRule.find(FieldTypeEnum.BUSINESS.getType()).orElseThrow();

    assertEquals("BUSINESS", businessRule.protocolFieldType());
    assertEquals(userRule.supportedSymbols(), businessRule.supportedSymbols());
  }

  @Test
  void shouldPreserveNumericAndTemporalSubtypesForFilterControls() {
    assertEquals("NUM_INT", ListFilterFieldTypeRule.find(FieldTypeEnum.NUM_INT.getType()).orElseThrow().protocolFieldType());
    assertEquals("NUM_DOUBLE", ListFilterFieldTypeRule.find(FieldTypeEnum.NUM_DOUBLE.getType()).orElseThrow().protocolFieldType());
    assertEquals("AMOUNT", ListFilterFieldTypeRule.find(FieldTypeEnum.AMOUNT.getType()).orElseThrow().protocolFieldType());
    assertEquals("STOCK", ListFilterFieldTypeRule.find(FieldTypeEnum.STOCK.getType()).orElseThrow().protocolFieldType());
    assertEquals("TIME", ListFilterFieldTypeRule.find(FieldTypeEnum.TIME.getType()).orElseThrow().protocolFieldType());
    assertEquals(List.of("EQ", "GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"),
        ListFilterFieldTypeRule.find(FieldTypeEnum.DATE.getType()).orElseThrow().supportedSymbols());
    assertEquals(List.of("GE", "LE", "BETWEEN", "IS_EMPTY", "IS_NOT_EMPTY"),
        ListFilterFieldTypeRule.find(FieldTypeEnum.TIME.getType()).orElseThrow().supportedSymbols());
  }

  @Test
  void shouldUseJsonContainsOperatorsForSingleSelectFields() {
    ListFilterFieldTypeRule rule = ListFilterFieldTypeRule.find(FieldTypeEnum.COMB.getType()).orElseThrow();

    assertEquals("ENUM", rule.protocolFieldType());
    assertEquals(List.of("CONTAINS", "NOT_CONTAINS", "IS_EMPTY", "IS_NOT_EMPTY"), rule.supportedSymbols());
  }

  @Test
  void shouldUseMultiValueOperatorsForMultiSelectFields() {
    ListFilterFieldTypeRule rule = ListFilterFieldTypeRule.find(FieldTypeEnum.COMB_MULTI.getType()).orElseThrow();

    assertEquals("ENUM_MULTI", rule.protocolFieldType());
    assertEquals(List.of("CONTAINS", "NOT_CONTAINS", "CONTAINS_ALL", "NOT_CONTAINS_ALL", "IS_EMPTY", "IS_NOT_EMPTY"), rule.supportedSymbols());
  }
}
