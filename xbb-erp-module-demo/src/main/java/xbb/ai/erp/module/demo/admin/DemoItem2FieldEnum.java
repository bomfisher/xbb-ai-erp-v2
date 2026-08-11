package xbb.ai.erp.module.demo.admin;

import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;

@Getter
public enum DemoItem2FieldEnum {
    NAME("name", "名称", FieldTypeEnum.TEXT, true);

    private final String attr;
    private final String attrName;
    private final Integer fieldType;
    private final Boolean required;

    DemoItem2FieldEnum(String attr, String attrName, FieldTypeEnum fieldType, Boolean required) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType.getType();
        this.required = required;
    }
}
