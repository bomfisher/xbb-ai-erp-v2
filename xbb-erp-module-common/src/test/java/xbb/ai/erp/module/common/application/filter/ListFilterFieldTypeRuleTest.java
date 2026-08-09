package xbb.ai.erp.module.common.application.filter;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
