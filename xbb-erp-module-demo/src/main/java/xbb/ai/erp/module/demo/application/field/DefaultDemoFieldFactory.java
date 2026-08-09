package xbb.ai.erp.module.demo.application.field;

import xbb.ai.erp.module.demo.admin.DemoFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.List;

public class DefaultDemoFieldFactory implements DemoFieldFactory {
    @Override
    public String sceneKey() {
        return "demo.archive";
    }

    @Override
    public List<SceneFieldMeta> getFields(SceneTypeEnum sceneType) {
        return java.util.Arrays.stream(DemoFieldEnum.values())
            .filter(field -> field.supports(sceneType))
            .map(this::toMeta)
            .map(SceneFieldMeta.class::cast)
            .toList();
    }

    private DemoFieldMeta toMeta(DemoFieldEnum field) {
        DemoFieldMeta meta = new DemoFieldMeta(field.getAttr(), field.getAttrName(), field.getFieldType(), field == DemoFieldEnum.NAME ? 1 : 0, 1)
            .withItemList(field.itemList());
        List<DemoFieldMeta> subFields = field.getSubFields().stream()
            .map(subField -> new DemoFieldMeta(
                subField.attr(),
                subField.attrName(),
                subField.fieldType().getType(),
                subField.required() ? 1 : 0,
                subField.editable() ? 1 : 0
            ))
            .toList();
        return meta.withSubFields(subFields);
    }
}
