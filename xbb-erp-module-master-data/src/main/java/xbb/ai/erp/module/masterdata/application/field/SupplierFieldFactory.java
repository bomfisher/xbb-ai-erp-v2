package xbb.ai.erp.module.masterdata.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.masterdata.admin.SupplierFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class SupplierFieldFactory {
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
        return Arrays.stream(SupplierFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(SupplierFieldEnum::toSceneFieldMeta)
            .toList();
    }
}
