package xbb.ai.erp.module.product.application.support;

import xbb.ai.erp.base.common.filed.FieldEntity;

import java.util.List;

public enum ProductCategoryFieldEnum {
    CATEGORY_CODE("categoryCode", "分类编码", "input", 1, 1),
    CATEGORY_NAME("categoryName", "分类名称", "input", 1, 1),
    PARENT_ID("parentId", "父级分类", "input-number", 0, 1),
    CATEGORY_LEVEL("categoryLevel", "分类层级", "input-number", 0, 1),
    SORT_NO("sortNo", "排序", "input-number", 0, 1),
    ENABLE_STATUS("enableStatus", "启用状态", "select", 1, 1);

    private final String attr;
    private final String attrName;
    private final String fieldType;
    private final Integer required;
    private final Integer editable;

    ProductCategoryFieldEnum(String attr, String attrName, String fieldType, Integer required, Integer editable) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
        this.required = required;
        this.editable = editable;
    }

    public FieldEntity toFieldEntity() {
        FieldEntity fieldEntity = new FieldEntity();
        fieldEntity.setAttr(attr);
        fieldEntity.setAttrName(attrName);
        fieldEntity.setFieldType(fieldType);
        fieldEntity.setRequired(required);
        fieldEntity.setEditable(editable);
        return fieldEntity;
    }

    public static List<FieldEntity> listHead() {
        return List.of(
            CATEGORY_CODE.toFieldEntity(),
            CATEGORY_NAME.toFieldEntity(),
            PARENT_ID.toFieldEntity(),
            CATEGORY_LEVEL.toFieldEntity(),
            SORT_NO.toFieldEntity(),
            ENABLE_STATUS.toFieldEntity()
        );
    }

    public static List<FieldEntity> formHead() {
        return listHead();
    }
}
