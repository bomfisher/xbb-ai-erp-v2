package xbb.ai.erp.scene.meta;

import java.util.List;

public interface SceneFieldRule {

    List<SceneFieldMeta> apply(List<SceneFieldMeta> fields);
}
