package xbb.ai.erp.scene.meta;

import xbb.ai.erp.base.common.filed.FieldEntity;

import java.util.List;

public final class SceneFieldAssembler {

    private SceneFieldAssembler() {
    }

    public static List<FieldEntity> buildHeadList(List<SceneFieldMeta> definitions) {
        return definitions.stream().map(SceneFieldAssembler::build).toList();
    }

    public static FieldEntity build(SceneFieldMeta definition) {
        FieldEntity entity = new FieldEntity();
        entity.setAttr(definition.getAttr());
        entity.setAttrName(definition.getAttrName());
        entity.setFieldType(String.valueOf(definition.getFieldType()));
        entity.setRequired(definition.getRequired());
        entity.setEditable(definition.getEditable());
        entity.setItemList(definition.getItemList());
        entity.setSubField(buildHeadList(definition.getSubFields()));
        if (definition.getBusinessCode() != null && !definition.getBusinessCode().isBlank()) {
            FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
            config.setBusinessCode(definition.getBusinessCode());
            entity.setBusinessSelectConfig(config);
        }
        return entity;
    }
}
