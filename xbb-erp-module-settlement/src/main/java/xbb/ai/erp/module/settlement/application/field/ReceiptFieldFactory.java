package xbb.ai.erp.module.settlement.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.settlement.admin.ReceiptFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class ReceiptFieldFactory {
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
        return Arrays.stream(ReceiptFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(ReceiptFieldEnum::toSceneFieldMeta)
            .toList();
    }
}
