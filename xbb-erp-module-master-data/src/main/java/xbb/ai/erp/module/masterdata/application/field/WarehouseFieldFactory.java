package xbb.ai.erp.module.masterdata.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.masterdata.admin.WarehouseFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class WarehouseFieldFactory {
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
        return Arrays.stream(WarehouseFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(WarehouseFieldEnum::toSceneFieldMeta)
            .toList();
    }
}
