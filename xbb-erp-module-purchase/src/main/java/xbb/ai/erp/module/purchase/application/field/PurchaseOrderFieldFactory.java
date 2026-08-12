package xbb.ai.erp.module.purchase.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.purchase.admin.PurchaseOrderFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class PurchaseOrderFieldFactory {
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
        return Arrays.stream(PurchaseOrderFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(PurchaseOrderFieldEnum::toSceneFieldMeta)
            .toList();
    }
}
