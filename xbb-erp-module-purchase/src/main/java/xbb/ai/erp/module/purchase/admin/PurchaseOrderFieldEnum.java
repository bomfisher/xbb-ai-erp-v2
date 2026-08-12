package xbb.ai.erp.module.purchase.admin;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum PurchaseOrderFieldEnum {
    ORDER_NO("main.orderNo", "order_no", FieldTypeEnum.TEXT, "order_no", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    SUPPLIER_ID("main.supplierId", "supplier_id", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "SUPPLIER", List.of()),
    ORDER_DATE("main.orderDate", "order_date", FieldTypeEnum.DATE, "order_date", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    EXPECTED_DATE("main.expectedDate", "expected_date", FieldTypeEnum.DATE, "expected_date", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    TOTAL_AMOUNT("main.totalAmount", "total_amount", FieldTypeEnum.AMOUNT, "total_amount", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    STATUS("main.status", "status", FieldTypeEnum.COMB, "status", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), "1:启用,0:禁用", null, List.of()),
    REMARK("main.remark", "remark", FieldTypeEnum.TEXT, "remark", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    CREATOR_ID("main.creatorId", "creator_id", FieldTypeEnum.USER, "creator_id", false, false, List.of(SceneTypeEnum.LIST), null, null, List.of()),
    MODIFY_ID("main.modifyId", "modify_id", FieldTypeEnum.USER, "modify_id", false, false, List.of(SceneTypeEnum.LIST), null, null, List.of());

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

    PurchaseOrderFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode, List<SceneFieldMeta> subFields) {
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
