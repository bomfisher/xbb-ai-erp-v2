package xbb.ai.erp.module.product.application.support;

import xbb.ai.erp.base.common.filed.FieldEntity;

import java.util.List;

public enum WarehouseFieldEnum {
    BIZ_ORG_ID("bizOrgId", "业务组织", "input-number", 1, 1),
    WAREHOUSE_CODE("warehouseCode", "仓库编码", "input", 1, 1),
    WAREHOUSE_NAME("warehouseName", "仓库名称", "input", 1, 1),
    WAREHOUSE_TYPE("warehouseType", "仓库类型", "input", 1, 1),
    ENABLE_STATUS("enableStatus", "启用状态", "select", 1, 1),
    ADDRESS("address", "地址", "textarea", 0, 1),
    MANAGER_ID("managerId", "负责人", "input", 0, 1),
    BIZ_STATUS("bizStatus", "业务状态", "input", 1, 1),
    ADD_TIME("addTime", "创建时间", "datetime", 0, 0),
    UPDATE_TIME("updateTime", "更新时间", "datetime", 0, 0);

    private final String attr;
    private final String attrName;
    private final String fieldType;
    private final Integer required;
    private final Integer editable;

    WarehouseFieldEnum(String attr, String attrName, String fieldType, Integer required, Integer editable) {
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
            BIZ_ORG_ID.toFieldEntity(),
            WAREHOUSE_CODE.toFieldEntity(),
            WAREHOUSE_NAME.toFieldEntity(),
            WAREHOUSE_TYPE.toFieldEntity(),
            ENABLE_STATUS.toFieldEntity(),
            ADDRESS.toFieldEntity(),
            MANAGER_ID.toFieldEntity(),
            BIZ_STATUS.toFieldEntity(),
            ADD_TIME.toFieldEntity(),
            UPDATE_TIME.toFieldEntity()
        );
    }

    public static List<FieldEntity> formHead() {
        return List.of(
            BIZ_ORG_ID.toFieldEntity(),
            WAREHOUSE_CODE.toFieldEntity(),
            WAREHOUSE_NAME.toFieldEntity(),
            WAREHOUSE_TYPE.toFieldEntity(),
            ENABLE_STATUS.toFieldEntity(),
            ADDRESS.toFieldEntity(),
            MANAGER_ID.toFieldEntity(),
            BIZ_STATUS.toFieldEntity()
        );
    }
}
