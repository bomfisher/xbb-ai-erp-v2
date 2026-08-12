package xbb.ai.erp.module.masterdata.admin;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum CustomerFieldEnum {
    CUSTOMER_CODE("main.customerCode", "客户编码", FieldTypeEnum.TEXT, "customer_code", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    CUSTOMER_NAME("main.customerName", "客户名称", FieldTypeEnum.TEXT, "customer_name", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    CONTACTS("contacts", "联系人", FieldTypeEnum.SUB_ITEM, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of(new SceneFieldMeta("name", "联系人名称", FieldTypeEnum.TEXT.getType(), 0, 1), new SceneFieldMeta("mobile", "手机号", FieldTypeEnum.TEXT.getType(), 0, 1), new SceneFieldMeta("defaultFlag", "默认联系人", FieldTypeEnum.SWITCH.getType(), 0, 1))),
    ADDRESS("main.address", "地址", FieldTypeEnum.ADDRESS, null, false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    ENABLED("main.enabled", "启用状态", FieldTypeEnum.COMB, "enabled", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), "0:禁用,1:启用", null, List.of()),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT, null, false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    CREATOR_ID("main.creatorId", "创建人", FieldTypeEnum.USER, "creator_id", false, false, List.of(SceneTypeEnum.LIST), null, null, List.of()),
    MODIFY_ID("main.modifyId", "修改人", FieldTypeEnum.USER, "modify_id", false, false, List.of(SceneTypeEnum.LIST), null, null, List.of());

    private final String attr;
    private final String attrName;
    private final FieldTypeEnum fieldType;
    private final String filterName;
    private final boolean required;
    private final boolean editable;
    private final List<SceneTypeEnum> scenes;
    private final String options;
    private final String businessCode;
    private final List<SceneFieldMeta> subFields;

    CustomerFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode, List<SceneFieldMeta> subFields) {
        this.attr = attr; this.attrName = attrName; this.fieldType = fieldType; this.filterName = filterName;
        this.required = required; this.editable = editable; this.scenes = scenes; this.options = options;
        this.businessCode = businessCode; this.subFields = subFields;
    }

    public boolean supports(SceneTypeEnum scene) {
        return scenes.contains(scene) && (fieldType != FieldTypeEnum.SUB_ITEM || scene != SceneTypeEnum.LIST);
    }

    public SceneFieldMeta toSceneFieldMeta() {
        return new SceneFieldMeta(attr, attrName, fieldType.getType(), required ? 1 : 0, editable ? 1 : 0, itemList(), businessCode, subFields);
    }

    public List<FieldItem> itemList() {
        if (options == null || options.isBlank()) return List.of();
        return Arrays.stream(options.split(",")).map(String::trim).filter(option -> !option.isEmpty()).map(option -> {
            String[] parts = option.split(":", 2); FieldItem item = new FieldItem(); item.setValue(parts[0].trim());
            item.setText(parts.length == 2 ? parts[1].trim() : parts[0].trim()); return item;
        }).toList();
    }

    public FieldEntity.BusinessSelectConfig businessSelectConfig() {
        if (businessCode == null || businessCode.isBlank()) return null;
        FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
        config.setBusinessCode(businessCode);
        return config;
    }
}
