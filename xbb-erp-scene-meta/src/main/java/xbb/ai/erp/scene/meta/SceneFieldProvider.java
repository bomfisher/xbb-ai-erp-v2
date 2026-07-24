package xbb.ai.erp.scene.meta;

import java.util.List;

public interface SceneFieldProvider {

    String sceneKey();

    List<SceneFieldMeta> getFields(SceneTypeEnum sceneType);
}
