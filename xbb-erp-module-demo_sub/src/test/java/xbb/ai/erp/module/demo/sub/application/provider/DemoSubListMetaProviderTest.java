package xbb.ai.erp.module.demo.sub.application.provider;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.demo.sub.application.field.DefaultDemoSubFieldFactory;

class DemoSubListMetaProviderTest {

  @Test
  void shouldBuildSelectConfigForFilterFields() {
    DemoSubListMetaProvider provider =
        new DemoSubListMetaProvider(new DefaultDemoSubFieldFactory());
    ListCommonQueryDTO dto = new ListCommonQueryDTO();
    dto.setCorpid("corp-001");

    FilterField dataIdFilter =
        provider.buildFilterMeta(dto).stream()
            .filter(field -> "dataId".equals(field.getAttr()))
            .findFirst()
            .orElseThrow();

    assertEquals("BUSINESS", dataIdFilter.getFieldType());
    assertTrue(dataIdFilter.getSupportedSymbols().contains("EQ"));
    assertNotNull(dataIdFilter.getBusinessSelectConfig());
    assertEquals("demo", dataIdFilter.getBusinessSelectConfig().getBusinessType());
    assertEquals("corp-001", dataIdFilter.getBusinessSelectConfig().getRequestPayload().get("corpid"));

    assertSelectConfig(provider, dto, "userId", "member", "/erp/v1/org/memberSelect/quickSearch");
    assertSelectConfig(provider, dto, "departmentId", "department", "/erp/v1/org/departmentSelect/quickSearch");
  }

  private void assertSelectConfig(
      DemoSubListMetaProvider provider,
      ListCommonQueryDTO dto,
      String attr,
      String businessType,
      String quickSearchUrl) {
    FilterField filter =
        provider.buildFilterMeta(dto).stream()
            .filter(field -> attr.equals(field.getAttr()))
            .findFirst()
            .orElseThrow();

    assertEquals("ID", filter.getFieldType());
    assertNotNull(filter.getBusinessSelectConfig());
    assertEquals(businessType, filter.getBusinessSelectConfig().getBusinessType());
    assertEquals(quickSearchUrl, filter.getBusinessSelectConfig().getQuickSearchUrl());
    assertEquals("corp-001", filter.getBusinessSelectConfig().getRequestPayload().get("corpid"));
  }
}
