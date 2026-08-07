package xbb.ai.erp.module.demo.application.field;

import xbb.ai.erp.scene.meta.SceneFieldProvider;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.List;

public interface DemoFieldFactory extends SceneFieldProvider {
    @Override
    List<SceneFieldMeta> getFields(SceneTypeEnum sceneType);
}
