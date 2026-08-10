package xbb.ai.erp.module.demo.sub.application.provider;

import java.util.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.base.common.pojo.ListButtonItemPojo;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterFieldTypeRule;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.demo.sub.admin.DemoSubFieldEnum;
import xbb.ai.erp.module.demo.sub.application.assembler.DemoSubFieldAssembler;
import xbb.ai.erp.module.demo.sub.application.field.DemoSubFieldFactory;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
@RequiredArgsConstructor
public class DemoSubListMetaProvider implements ListMetaProvider {
  private final DemoSubFieldFactory fieldFactory;

  public String businessCode() {
    return BusinessCodeEnum.DEMO_SUB.getCode();
  }

  public static Map<String, ListFilterMetaPojo> conditionMetaMap() {
    Map<String, ListFilterMetaPojo> map = new LinkedHashMap<>();
    for (DemoSubFieldEnum field : DemoSubFieldEnum.values())
      if (field.getFilterName() != null) {
        var rule = ListFilterFieldTypeRule.find(field.getFieldType()).orElseThrow();
        String attr = field.getAttr().substring(field.getAttr().lastIndexOf('.') + 1);
        map.put(
            attr,
            new ListFilterMetaPojo(
                attr,
                field.getFilterName(),
                rule.protocolFieldType(),
                rule.supportedSymbols()));
      }
    return Collections.unmodifiableMap(map);
  }

  public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
    return conditionMetaMap().values().stream()
        .map(
            meta -> {
              FilterField field = new FilterField();
              DemoSubFieldEnum definition = Arrays.stream(DemoSubFieldEnum.values())
                  .filter(def -> def.getAttr().endsWith("." + meta.getAttr()))
                  .findFirst()
                  .orElseThrow();
              field.setAttr(meta.getAttr());
              field.setAttrName(definition.getAttrName());
              field.setFieldType(String.valueOf(definition.getFieldType()));
              field.setFilterFieldType(meta.getFieldType());
              field.setSupportedSymbols(new ArrayList<>(meta.getSupportedSymbols()));
              field.setItemList(List.of());
              field.setSourceFieldType(definition.getFieldType());
              field.setBusinessSelectConfig(buildBusinessSelectConfig(definition.getBusinessCode()));
              return field;
            })
        .toList();
  }

  public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {
    return conditionMetaMap();
  }

  public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
    return DemoSubFieldAssembler.buildHeadList(fieldFactory.getFields(SceneTypeEnum.LIST));
  }

  public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
    ListMetaBundlePojo bundle = new ListMetaBundlePojo();
    bundle.setTopButtonList(List.of(new ListButtonItemPojo("ADD", "新增", 10, "ADD")));
    return bundle;
  }

  public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
    return new ListMetaBundlePojo();
  }

  public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
    return new ListMetaBundlePojo();
  }

  private FieldEntity.BusinessSelectConfig buildBusinessSelectConfig(String businessCode) {
    if (businessCode == null) {
      return null;
    }
    FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
    config.setBusinessCode(businessCode);
    return config;
  }
}
