package xbb.ai.erp.module.demo.sub.application.field;

import java.util.Arrays;
import java.util.List;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.demo.sub.admin.DemoSubFieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class DefaultDemoSubFieldFactory implements DemoSubFieldFactory {
  public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
    return Arrays.stream(DemoSubFieldEnum.values())
        .filter(field -> field.supports(scene))
        .map(
            field ->
                new SceneFieldMeta(
                    field.getAttr(),
                    field.getAttrName(),
                    field.getFieldType(),
                    field == DemoSubFieldEnum.DATA_ID || field == DemoSubFieldEnum.NAME ? 1 : 0,
                    field.getFieldType() == null ? 0 : 1,
                    List.of()))
        .toList();
  }
}
