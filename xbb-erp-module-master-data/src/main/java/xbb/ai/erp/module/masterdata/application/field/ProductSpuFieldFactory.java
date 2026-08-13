package xbb.ai.erp.module.masterdata.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.masterdata.admin.ProductSpuFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class ProductSpuFieldFactory {
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
        return Arrays.stream(ProductSpuFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(ProductSpuFieldEnum::toSceneFieldMeta)
            .toList();
    }
}
