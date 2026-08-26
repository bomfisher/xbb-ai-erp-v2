package xbb.ai.erp.module.masterdata.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.masterdata.admin.FundAccountFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class FundAccountFieldFactory {
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
        return Arrays.stream(FundAccountFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(FundAccountFieldEnum::toSceneFieldMeta)
            .toList();
    }
}
