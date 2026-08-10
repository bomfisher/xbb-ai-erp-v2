package xbb.ai.erp.module.demo.application.provider;

import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.pojo.FilterField;
import xbb.ai.erp.base.common.pojo.ListButtonItemPojo;
import xbb.ai.erp.module.demo.admin.DemoFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.module.common.admin.dto.ListCommonQueryDTO;
import xbb.ai.erp.module.common.application.filter.ListFilterMetaPojo;
import xbb.ai.erp.module.common.application.filter.ListFilterFieldTypeRule;
import xbb.ai.erp.module.common.application.pojo.ListMetaBundlePojo;
import xbb.ai.erp.module.common.application.provider.ListMetaProvider;


@Component
public class DemoListMetaProvider implements ListMetaProvider {

    @Override
    public String businessCode() {
        return BusinessCodeEnum.DEMO.getCode();
    }

    @Override
    public List<FilterField> buildFilterMeta(ListCommonQueryDTO dto) {
        return Arrays.stream(DemoFieldEnum.values())
            .filter(field -> field.getFilterName() != null)
            .map(this::buildFilterField)
            .toList();
    }

    @Override
    public Map<String, ListFilterMetaPojo> buildFilterConditionMeta(ListCommonQueryDTO dto) {
        Map<String, ListFilterMetaPojo> metadata = new LinkedHashMap<>();
        Arrays.stream(DemoFieldEnum.values())
            .filter(field -> field.getFilterName() != null)
            .forEach(field -> metadata.put(field.getAttr(), new ListFilterMetaPojo(
                field.getAttr(), field.getFilterName(), filterRule(field).protocolFieldType(), filterRule(field).supportedSymbols())));
        return Map.copyOf(metadata);
    }

    @Override
    public List<FieldEntity> buildHeaderMeta(ListCommonQueryDTO dto) {
        return Arrays.stream(DemoFieldEnum.values())
            .filter(field -> field.supports(SceneTypeEnum.LIST))
            .map(this::buildHeaderField)
            .toList();
    }

    @Override
    public ListMetaBundlePojo buildTopButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setTopButtonList(List.of(new ListButtonItemPojo("ADD", "新建", 10, "ADD")));
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildBottomButtonMeta(ListCommonQueryDTO dto) {
        ListMetaBundlePojo bundle = new ListMetaBundlePojo();
        bundle.setBottomButtonList(List.of(new xbb.ai.erp.base.common.pojo.ListButtonItemPojo("DELETE", "删除", 10, "DELETE")));
        return bundle;
    }

    @Override
    public ListMetaBundlePojo buildRowActionMeta(ListCommonQueryDTO dto) {
        return new ListMetaBundlePojo();
    }

    private FilterField buildFilterField(DemoFieldEnum field) {
        FilterField result = new FilterField();
        result.setAttr(field.getAttr());
        result.setAttrName(field.getAttrName());
        result.setSourceFieldType(field.getFieldType());
        result.setFieldType(String.valueOf(field.getFieldType()));
        result.setFilterFieldType(filterRule(field).protocolFieldType());
        result.setSupportedSymbols(filterRule(field).supportedSymbols());
        result.setItemList(parseOptions(field.getOptions()));
        result.setBusinessSelectConfig(selectConfig(field.getBusinessCode()));
        return result;
    }

    private SceneFieldMeta toSceneMeta(DemoFieldEnum field) {
        return new SceneFieldMeta(field.getAttr(), field.getAttrName(), field.getFieldType(),
            Boolean.TRUE.equals(field.getRequired()) ? 1 : 0, 1, parseOptions(field.getOptions()), field.getBusinessCode());
    }

    private FieldEntity buildHeaderField(DemoFieldEnum field) {
        FieldEntity entity = SceneFieldAssembler.build(toSceneMeta(field));
        entity.setBusinessSelectConfig(selectConfig(field.getBusinessCode()));
        return entity;
    }

    private static ListFilterFieldTypeRule filterRule(DemoFieldEnum field) {
        return ListFilterFieldTypeRule.find(field.getFieldType())
            .orElseThrow(() -> new IllegalArgumentException("不支持的筛选字段类型: " + field.getFieldType()));
    }

    private static FieldEntity.BusinessSelectConfig selectConfig(String businessCode) {
        if (businessCode == null) {
            return null;
        }
        FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
        config.setBusinessCode(businessCode);
        return config;
    }

    private static List<FieldItem> parseOptions(String options) {
        if (options == null || options.isBlank()) {
            return List.of();
        }
        return Arrays.stream(options.split(",")).map(String::trim).filter(option -> !option.isEmpty()).map(option -> {
            String[] parts = option.split(":", 2);
            FieldItem item = new FieldItem();
            item.setValue(parts[0].trim());
            item.setText(parts.length == 2 ? parts[1].trim() : parts[0].trim());
            return item;
        }).toList();
    }
}
