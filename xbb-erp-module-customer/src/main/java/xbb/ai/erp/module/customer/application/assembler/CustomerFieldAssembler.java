package xbb.ai.erp.module.customer.application.assembler;

import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.scene.meta.SceneFieldAssembler;
import xbb.ai.erp.scene.meta.SceneFieldMeta;

import java.util.List;

public final class CustomerFieldAssembler {

    private CustomerFieldAssembler() {
    }

    public static List<FieldEntity> buildHeadList(List<? extends SceneFieldMeta> definitions) {
        return SceneFieldAssembler.buildHeadList(List.copyOf(definitions));
    }
}
