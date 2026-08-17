package xbb.ai.erp.module.sales.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.sales.admin.SalesOrderFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class SalesOrderFieldFactory {
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
        return Arrays.stream(SalesOrderFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(SalesOrderFieldEnum::toSceneFieldMeta)
            .toList();
    }
}
