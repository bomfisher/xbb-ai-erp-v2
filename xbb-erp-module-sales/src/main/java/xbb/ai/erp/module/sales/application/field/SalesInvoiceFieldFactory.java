package xbb.ai.erp.module.sales.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.sales.admin.SalesInvoiceFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class SalesInvoiceFieldFactory {
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
        return Arrays.stream(SalesInvoiceFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(SalesInvoiceFieldEnum::toSceneFieldMeta)
            .toList();
    }
}
