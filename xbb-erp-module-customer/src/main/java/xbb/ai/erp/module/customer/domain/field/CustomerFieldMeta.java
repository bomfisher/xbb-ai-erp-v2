package xbb.ai.erp.module.customer.domain.field;

import xbb.ai.erp.scene.meta.SceneFieldMeta;

public class CustomerFieldMeta extends SceneFieldMeta {

    public CustomerFieldMeta(String attr, String attrName, Integer fieldType, Integer required, Integer editable) {
        super(attr, attrName, fieldType, required, editable);
    }

    public CustomerFieldMeta withAttrName(String value) {
        return new CustomerFieldMeta(getAttr(), value, getFieldType(), getRequired(), getEditable());
    }

    public CustomerFieldMeta withEditable(Integer value) {
        return new CustomerFieldMeta(getAttr(), getAttrName(), getFieldType(), getRequired(), value);
    }
}
