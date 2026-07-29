package xbb.ai.erp.base.common.filed;

import lombok.Getter;

@Getter
public class FieldRule {

    private final String attr;
    private final String attrName;
    private final Integer fieldType;
    private final Integer maxLength;
    private final Integer required;

    public FieldRule(String attr, String attrName, Integer fieldType, Integer maxLength) {
        this(attr, attrName, fieldType, maxLength, 0);
    }

    public FieldRule(String attr, String attrName, Integer fieldType, Integer maxLength, Integer required) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
        this.maxLength = maxLength;
        this.required = required;
    }
}
