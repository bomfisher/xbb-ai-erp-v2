package xbb.ai.erp.base.common.filed;

import lombok.Getter;

@Getter
public enum FieldDemoEnum implements BusinessField {
    DEMO("demo", "例子", FieldTypeEnum.TEXT),
    ;
    //字段别名 用于接口传递
    private final String attr;
    //字段名 用于名称展示
    private final String attrName;
    //字段类型 参考 xbb/ai/erp/base/common/filed/FieldTypeEnum.java
    private final Integer fieldType;

    FieldDemoEnum(String attr, String attrName, FieldTypeEnum fieldType) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType.getType();
    }


}
