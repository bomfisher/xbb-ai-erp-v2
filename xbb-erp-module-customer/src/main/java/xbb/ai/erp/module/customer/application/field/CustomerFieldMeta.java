package xbb.ai.erp.module.customer.application.field;

import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.scene.meta.SceneFieldMeta;

import java.util.List;

public class CustomerFieldMeta extends SceneFieldMeta {

    public CustomerFieldMeta(String attr, String attrName, Integer fieldType, Integer required, Integer editable) {
        super(attr, attrName, fieldType, required, editable);
    }

    public CustomerFieldMeta(String attr, String attrName, Integer fieldType, Integer required, Integer editable, List<FieldItem> itemList) {
        super(attr, attrName, fieldType, required, editable, itemList);
    }

    public CustomerFieldMeta withAttrName(String value) {
        return new CustomerFieldMeta(getAttr(), value, getFieldType(), getRequired(), getEditable(), getItemList());
    }

    public CustomerFieldMeta withEditable(Integer value) {
        return new CustomerFieldMeta(getAttr(), getAttrName(), getFieldType(), getRequired(), value, getItemList());
    }

    public CustomerFieldMeta withItemList(List<FieldItem> value) {
        return new CustomerFieldMeta(getAttr(), getAttrName(), getFieldType(), getRequired(), getEditable(), value);
    }
}
