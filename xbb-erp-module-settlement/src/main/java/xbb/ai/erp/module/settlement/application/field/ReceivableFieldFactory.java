package xbb.ai.erp.module.settlement.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.settlement.admin.ReceivableFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class ReceivableFieldFactory {
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
        return Arrays.stream(ReceivableFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(ReceivableFieldEnum::toSceneFieldMeta)
            .toList();
    }
}
