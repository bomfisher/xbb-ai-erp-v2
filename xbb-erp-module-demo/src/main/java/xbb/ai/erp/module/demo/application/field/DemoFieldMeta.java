package xbb.ai.erp.module.demo.application.field;

import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.scene.meta.SceneFieldMeta;

import java.util.List;

public class DemoFieldMeta extends SceneFieldMeta {
    private final List<DemoFieldMeta> subFields;

    public DemoFieldMeta(String attr, String attrName, Integer fieldType, Integer required, Integer editable) {
        this(attr, attrName, fieldType, required, editable, List.of(), List.of());
    }

    public DemoFieldMeta withItemList(List<FieldItem> items) {
        return new DemoFieldMeta(getAttr(), getAttrName(), getFieldType(), getRequired(), getEditable(), items, subFields);
    }

    public DemoFieldMeta withSubFields(List<DemoFieldMeta> fields) {
        return new DemoFieldMeta(getAttr(), getAttrName(), getFieldType(), getRequired(), getEditable(), getItemList(), fields);
    }

    private DemoFieldMeta(String attr, String attrName, Integer fieldType, Integer required, Integer editable, List<FieldItem> items, List<DemoFieldMeta> subFields) {
        super(attr, attrName, fieldType, required, editable, items);
        this.subFields = List.copyOf(subFields);
    }

    public List<DemoFieldMeta> getSubFields() {
        return subFields;
    }
}
