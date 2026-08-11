package xbb.ai.erp.module.demo.application.field;

import java.util.Arrays;
import java.util.List;
import xbb.ai.erp.base.common.filed.FieldItem;
import org.springframework.stereotype.Component;
import xbb.ai.erp.module.demo.admin.DemoFieldEnum;
import xbb.ai.erp.module.demo.admin.DemoItemFieldEnum;
import xbb.ai.erp.module.demo.admin.DemoItem2FieldEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Component
public class DefaultDemoFieldFactory implements DemoFieldFactory {
    @Override
    public List<SceneFieldMeta> getFields(SceneTypeEnum scene) {
        return Arrays.stream(DemoFieldEnum.values())
            .filter(field -> field.supports(scene))
            .map(this::toSceneFieldMeta)
            .toList();
    }

    private SceneFieldMeta toSceneFieldMeta(DemoFieldEnum field) {
        return new SceneFieldMeta(field.getAttr(), field.getAttrName(), field.getFieldType(),
            Boolean.TRUE.equals(field.getRequired()) ? 1 : 0, 1, parseOptions(field.getOptions()),
            field.getBusinessCode(), subFields(field));
    }

    private static List<SceneFieldMeta> subFields(DemoFieldEnum field) {
        return switch (field) {
            case DEMO_ITEM -> demoItemFields();
            case DEMO_ITEM_2 -> demoItem2Fields();
            default -> List.of();
        };
    }

    private static List<SceneFieldMeta> demoItemFields() {
        return Arrays.stream(DemoItemFieldEnum.values())
            .map(field -> new SceneFieldMeta(field.getAttr(), field.getAttrName(), field.getFieldType(),
                Boolean.TRUE.equals(field.getRequired()) ? 1 : 0, 1, List.of()))
            .toList();
    }

    private static List<SceneFieldMeta> demoItem2Fields() {
        return Arrays.stream(DemoItem2FieldEnum.values())
            .map(field -> new SceneFieldMeta(field.getAttr(), field.getAttrName(), field.getFieldType(),
                Boolean.TRUE.equals(field.getRequired()) ? 1 : 0, 1, List.of()))
            .toList();
    }

    private static List<FieldItem> parseOptions(String options) {
        if (options == null || options.isBlank()) {
            return List.of();
        }
        return Arrays.stream(options.split(","))
            .map(String::trim)
            .filter(option -> !option.isEmpty())
            .map(DefaultDemoFieldFactory::toFieldItem)
            .toList();
    }

    private static FieldItem toFieldItem(String option) {
        String[] parts = option.split(":", 2);
        FieldItem item = new FieldItem();
        item.setValue(parts[0].trim());
        item.setText(parts.length == 2 ? parts[1].trim() : parts[0].trim());
        return item;
    }
}
