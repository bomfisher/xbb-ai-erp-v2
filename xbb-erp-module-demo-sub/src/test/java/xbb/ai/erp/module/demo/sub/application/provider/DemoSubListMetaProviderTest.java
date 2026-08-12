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

    assertEquals("16", dataIdFilter.getFieldType());
    assertEquals("BUSINESS", dataIdFilter.getFilterFieldType());
    assertTrue(dataIdFilter.getSupportedSymbols().contains("EQ"));
    assertNotNull(dataIdFilter.getBusinessSelectConfig());
    assertEquals("DEMO", dataIdFilter.getBusinessSelectConfig().getBusinessCode());

    assertSelectConfig(provider, dto, "userId", "ORG_MEMBER");
    assertSelectConfig(provider, dto, "departmentId", "ORG_DEPARTMENT");
  }

  @Test
  void shouldBuildEditRowAction() {
    DemoSubListMetaProvider provider = new DemoSubListMetaProvider(new DefaultDemoSubFieldFactory());

    var action = provider.buildRowActionMeta(new ListCommonQueryDTO()).getRowActionList().getFirst();

    assertEquals("EDIT", action.getActionCode());
    assertEquals("编辑", action.getActionName());
    assertEquals(10, action.getSort());
    assertEquals("PRIMARY", action.getShowMode());
    assertEquals("NONE", action.getConfirmType());
  }

  private void assertSelectConfig(
      DemoSubListMetaProvider provider,
      ListCommonQueryDTO dto,
      String attr,
      String businessCode) {
    FilterField filter =
        provider.buildFilterMeta(dto).stream()
            .filter(field -> attr.equals(field.getAttr()))
            .findFirst()
            .orElseThrow();

    assertEquals("12", filter.getFieldType());
    assertEquals("ID", filter.getFilterFieldType());
    assertNotNull(filter.getBusinessSelectConfig());
    assertEquals(businessCode, filter.getBusinessSelectConfig().getBusinessCode());
  }
}
