package xbb.ai.erp.module.product.application.support;

import xbb.ai.erp.base.common.filed.FieldEntity;

import java.util.List;

public enum ProductUnitFieldEnum {
    UNIT_CODE("unitCode", "单位编码", "input", 1, 1),
    UNIT_NAME("unitName", "单位名称", "input", 1, 1),
    PRECISION_NUM("precisionNum", "精度", "input-number", 0, 1),
    ENABLE_STATUS("enableStatus", "启用状态", "select", 1, 1);

    private final String attr;
    private final String attrName;
    private final String fieldType;
    private final Integer required;
    private final Integer editable;

    ProductUnitFieldEnum(String attr, String attrName, String fieldType, Integer required, Integer editable) {
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
            UNIT_CODE.toFieldEntity(),
            UNIT_NAME.toFieldEntity(),
            PRECISION_NUM.toFieldEntity(),
            ENABLE_STATUS.toFieldEntity()
        );
    }

    public static List<FieldEntity> formHead() {
        return listHead();
    }
}
