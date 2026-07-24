package xbb.ai.erp.module.customer.domain.field;

import xbb.ai.erp.scene.meta.SceneFieldProvider;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

import java.util.List;

public interface CustomerFieldFactory extends SceneFieldProvider {

    @Override
    List<SceneFieldMeta> getFields(SceneTypeEnum sceneType);

    default List<SceneFieldMeta> buildListFields() {
        return getFields(SceneTypeEnum.LIST);
    }

    default List<SceneFieldMeta> buildAddItemFields() {
        return getFields(SceneTypeEnum.CREATE);
    }

    default List<SceneFieldMeta> buildUpdateItemFields() {
        return getFields(SceneTypeEnum.UPDATE);
    }

    default List<SceneFieldMeta> buildDetailFields() {
        return getFields(SceneTypeEnum.DETAIL);
    }
}
