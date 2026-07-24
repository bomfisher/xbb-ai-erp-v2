package xbb.ai.erp.scene.meta;

import lombok.Getter;

@Getter
public class SceneFieldMeta {

    private final String attr;
    private final String attrName;
    private final Integer fieldType;
    private final Integer required;
    private final Integer editable;

    public SceneFieldMeta(String attr, String attrName, Integer fieldType, Integer required, Integer editable) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
        this.required = required;
        this.editable = editable;
    }
}
