package xbb.ai.erp.module.common.application.render;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.common.application.provider.ListMetaRegistry;

class ListValueRendererTest {

  @Test
  void shouldAggregateMemberValuesAcrossFieldsBeforeOneLookup() {
    AtomicInteger memberLookupCount = new AtomicInteger();
    AtomicReference<Set<String>> requestedValues = new AtomicReference<>();
    ListReferenceValueProvider memberProvider =
        new ListReferenceValueProvider() {
          @Override
          public ListReferenceKey key() {
            return new ListReferenceKey(String.valueOf(FieldTypeEnum.USER.getType()), "ORG_MEMBER");
          }

          @Override
          public Map<String, String> findDisplayMap(String corpid, Set<String> values) {
            memberLookupCount.incrementAndGet();
            requestedValues.set(values);
            return Map.of("u-1", "成员一", "u-2", "成员二");
          }
        };
    ListValueRenderer renderer =
        new ListValueRenderer(
            new ListMetaRegistry(List.of(new TestListMetaProvider())),
            new ListReferenceValueProviderRegistry(List.of(memberProvider)));

    List<Row> rows = List.of(new Row("u-1", "[\"u-2\"]"), new Row("u-2", "[\"u-1\"]"));

    renderer.render("corp-001", "TEST", rows);

    assertEquals(1, memberLookupCount.get());
    assertEquals(Set.of("u-1", "u-2"), requestedValues.get());
    assertEquals("成员一", rows.get(0).getUserId());
    assertEquals("成员二", rows.get(0).getCreatorId());
    assertEquals("成员二", rows.get(1).getUserId());
    assertEquals("成员一", rows.get(1).getCreatorId());
  }

  private static class TestListMetaProvider implements ListMetaProvider {
    @Override
    public String businessCode() {
      return "TEST";
    }

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
      return List.of();
    }

    @Override
    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {
      return Map.of();
    }

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
      return List.of(
          memberField("main.userId", FieldTypeEnum.USER),
          memberField("main.creatorId", FieldTypeEnum.USER_MULTI));
    }

    @Override
    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
      return new ListMetaBundlePojo();
    }

    @Override
    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
      return new ListMetaBundlePojo();
    }

    @Override
    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
      return new ListMetaBundlePojo();
    }

    private FieldEntity memberField(String attr, FieldTypeEnum fieldType) {
      FieldEntity field = new FieldEntity();
      field.setAttr(attr);
      field.setFieldType(String.valueOf(fieldType.getType()));
      FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
      config.setBusinessCode("ORG_MEMBER");
      field.setBusinessSelectConfig(config);
      return field;
    }
  }

  private static class Row {
    private String userId;
    private String creatorId;

    private Row(String userId, String creatorId) {
      this.userId = userId;
      this.creatorId = creatorId;
    }

    public String getUserId() {
      return userId;
    }

    public void setUserId(String userId) {
      this.userId = userId;
    }

    public String getCreatorId() {
      return creatorId;
    }

    public void setCreatorId(String creatorId) {
      this.creatorId = creatorId;
    }
  }
}
