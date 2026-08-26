package xbb.ai.erp.scene.meta;

import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.ProductSelectSourceModeEnum;

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
    private final List<SceneFieldMeta> subFields;
    private final ProductSelectSourceModeEnum productSelectSourceMode;

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
        this(attr, attrName, fieldType, required, editable, itemList, businessCode, List.of());
    }

    public SceneFieldMeta(
        String attr,
        String attrName,
        Integer fieldType,
        Integer required,
        Integer editable,
        List<FieldItem> itemList,
        String businessCode,
        List<SceneFieldMeta> subFields
    ) {
        this(attr, attrName, fieldType, required, editable, itemList, businessCode, subFields, null);
    }

    public SceneFieldMeta(
        String attr,
        String attrName,
        Integer fieldType,
        Integer required,
        Integer editable,
        List<FieldItem> itemList,
        String businessCode,
        List<SceneFieldMeta> subFields,
        ProductSelectSourceModeEnum productSelectSourceMode
    ) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
        this.required = required;
        this.editable = editable;
        this.itemList = itemList == null ? List.of() : List.copyOf(itemList);
        this.businessCode = businessCode;
        this.subFields = subFields == null ? List.of() : List.copyOf(subFields);
        this.productSelectSourceMode = productSelectSourceMode;
    }
}
