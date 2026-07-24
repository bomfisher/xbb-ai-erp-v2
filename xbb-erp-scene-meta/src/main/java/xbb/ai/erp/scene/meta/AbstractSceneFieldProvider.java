package xbb.ai.erp.scene.meta;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractSceneFieldProvider implements SceneFieldProvider {

    private final List<SceneFieldRule> rules;

    protected AbstractSceneFieldProvider(List<SceneFieldRule> rules) {
        this.rules = rules;
    }

    @Override
    public final List<SceneFieldMeta> getFields(SceneTypeEnum sceneType) {
        List<SceneFieldMeta> fields = new ArrayList<>(buildFields(sceneType));
        for (SceneFieldRule rule : rules) {
            fields = rule.apply(fields);
        }
        return fields;
    }

    protected abstract List<SceneFieldMeta> buildFields(SceneTypeEnum sceneType);
}
