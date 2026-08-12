package xbb.ai.erp.module.purchase.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.purchase.admin.PurchaseInboundFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class PurchaseInboundFieldFactory {
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
        return Arrays.stream(PurchaseInboundFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(PurchaseInboundFieldEnum::toSceneFieldMeta)
            .toList();
    }
}
