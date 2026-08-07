package xbb.ai.erp.module.demo.application.provider;

import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;
import xbb.ai.erp.module.common.application.filter.ListFilterFieldTypeRule;
import xbb.ai.erp.module.demo.admin.DemoFieldEnum;
import xbb.ai.erp.module.demo.application.assembler.DemoFieldAssembler;
import xbb.ai.erp.module.demo.application.field.DemoFieldFactory;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class DemoListMetaProvider implements ListMetaProvider {
    private static final List<Definition> DEFINITIONS = java.util.Arrays.stream(DemoFieldEnum.values())
        .filter(field -> field.getFilterName() != null)
        .map(Definition::from)
        .toList();
    private static final Map<String, ListFilterMetaPojo> CONDITION_META_MAP = buildConditionMetaMap();
    private final DemoFieldFactory demoFieldFactory;

    public DemoListMetaProvider(DemoFieldFactory demoFieldFactory) {
        this.demoFieldFactory = demoFieldFactory;
    }

    @Override
    public String businessCode() {
        return BusinessCodeEnum.DEMO.getCode();
    }

    public static Map<String, ListFilterMetaPojo> conditionMetaMap() {
        return CONDITION_META_MAP;
    }

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
        return DEFINITIONS.stream().map(definition -> {
            FilterField field = new FilterField();
            field.setAttr(definition.attr());
            field.setAttrName(definition.attrName());
            field.setFieldType(definition.fieldType());
            field.setSupportedSymbols(definition.symbols());
            return field;
        }).toList();
    }

    @Override
    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {
        return CONDITION_META_MAP;
    }

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
        return DemoFieldAssembler.buildHeadList(demoFieldFactory.getFields(SceneTypeEnum.LIST));
    }

    @Override
    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setTopButtonList(List.of());
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setBottomButtonList(List.of());
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setRowActionList(List.of());
        return bundle;
    }

    private static Map<String, ListFilterMetaPojo> buildConditionMetaMap() {
        Map<String, ListFilterMetaPojo> map = new LinkedHashMap<>();
        for (Definition definition : DEFINITIONS) {
            map.put(definition.attr(), new ListFilterMetaPojo(definition.attr(), definition.column(), definition.fieldType(), Set.copyOf(definition.symbols())));
        }
        return Collections.unmodifiableMap(map);
    }

    private record Definition(String attr, String attrName, String fieldType, String column, List<String> symbols) {
        private static Definition from(DemoFieldEnum field) {
            ListFilterFieldTypeRule rule = ListFilterFieldTypeRule.find(field.getFieldType())
                .orElseThrow(() -> new IllegalStateException("字段类型不支持筛选：" + field.name()));
            String attr = field.getAttr().substring(field.getAttr().lastIndexOf('.') + 1);
            return new Definition(attr, field.getAttrName(), rule.protocolFieldType(), field.getFilterName(), rule.supportedSymbols());
        }
    }
}
