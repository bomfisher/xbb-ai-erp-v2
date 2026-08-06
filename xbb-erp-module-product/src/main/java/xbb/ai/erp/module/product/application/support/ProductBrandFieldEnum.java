package xbb.ai.erp.module.product.application.support;

import xbb.ai.erp.base.common.filed.FieldEntity;

import java.util.List;

public enum ProductBrandFieldEnum {
    BRAND_CODE("brandCode", "品牌编码", "input", 1, 1),
    BRAND_NAME("brandName", "品牌名称", "input", 1, 1),
    SORT_NO("sortNo", "排序", "input-number", 0, 1),
    ENABLE_STATUS("enableStatus", "启用状态", "select", 1, 1);

    private final String attr;
    private final String attrName;
    private final String fieldType;
    private final Integer required;
    private final Integer editable;

    ProductBrandFieldEnum(String attr, String attrName, String fieldType, Integer required, Integer editable) {
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
            BRAND_CODE.toFieldEntity(),
            BRAND_NAME.toFieldEntity(),
            SORT_NO.toFieldEntity(),
            ENABLE_STATUS.toFieldEntity()
        );
    }

    public static List<FieldEntity> formHead() {
        return listHead();
    }
}
