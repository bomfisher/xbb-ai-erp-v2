package xbb.ai.erp.module.demo.application.assembler;

import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.module.demo.application.field.DemoFieldMeta;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneFieldMeta;

import java.util.List;

public final class DemoFieldAssembler {
    private DemoFieldAssembler() {
    }

    public static List<FieldEntity> buildHeadList(List<? extends SceneFieldMeta> fields) {
        return fields.stream().map(DemoFieldAssembler::toFieldEntity).toList();
    }

    private static FieldEntity toFieldEntity(SceneFieldMeta field) {
        FieldEntity entity = SceneFieldAssembler.build(field);
        if (field instanceof DemoFieldMeta demoField && !demoField.getSubFields().isEmpty()) {
            entity.setSubField(demoField.getSubFields().stream().map(DemoFieldAssembler::toFieldEntity).toList());
        }
        return entity;
    }
}
