package xbb.ai.erp.module.demo.application.field;

import java.util.List;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

public interface DemoFieldFactory {
    List<SceneFieldMeta> getFields(SceneTypeEnum sceneType);
}
