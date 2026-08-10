package xbb.ai.erp.scene.meta;

import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldItem;

import java.util.List;

@Getter
public class SceneFieldMeta {

    private final String attr;
    private final String attrName;
    private final Integer fieldType;
    private final Integer required;
    private final Integer editable;
    private final List<FieldItem> itemList;
    private final String businessCode;

    public SceneFieldMeta(String attr, String attrName, Integer fieldType, Integer required, Integer editable) {
        this(attr, attrName, fieldType, required, editable, List.of());
    }

    public SceneFieldMeta(String attr, String attrName, Integer fieldType, Integer required, Integer editable, List<FieldItem> itemList) {
        this(attr, attrName, fieldType, required, editable, itemList, null);
    }

    public SceneFieldMeta(
        String attr,
        String attrName,
        Integer fieldType,
        Integer required,
        Integer editable,
        List<FieldItem> itemList,
        String businessCode
    ) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
        this.required = required;
        this.editable = editable;
        this.itemList = itemList == null ? List.of() : List.copyOf(itemList);
        this.businessCode = businessCode;
    }
}
