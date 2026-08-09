package xbb.ai.erp.module.demo.sub.application.field;

import java.util.List;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

public interface DemoSubFieldFactory {
  List<SceneFieldMeta> getFields(SceneTypeEnum sceneType);
}
