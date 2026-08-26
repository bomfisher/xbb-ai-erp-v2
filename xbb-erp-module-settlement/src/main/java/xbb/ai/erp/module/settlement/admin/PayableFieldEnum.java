package xbb.ai.erp.module.settlement.admin;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldRule;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum PayableFieldEnum {
    PAYABLE_NO("main.payableNo", "应付款编号", FieldTypeEnum.SERIAL_NO, null, true, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    SUPPLIER_ID("main.supplierId", "供应商", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "SUPPLIER"),
    SOURCE_TYPE("main.sourceType", "来源类型", FieldTypeEnum.COMB, "source_type", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), "PURCHASE_INVOICE:采购发票,OPENING_BALANCE:期初,MANUAL_ADJUSTMENT:手工调整", null),
    SOURCE_INVOICE_ID("main.sourceInvoiceId", "来源采购发票", FieldTypeEnum.BUSINESS, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "PURCHASE_INVOICE"),
    PAYABLE_DATE("main.payableDate", "应付日期", FieldTypeEnum.DATE, "payable_date", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    DUE_DATE("main.dueDate", "到期日", FieldTypeEnum.DATE, "due_date", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    AMOUNT("main.amount", "应付金额", FieldTypeEnum.AMOUNT, "amount", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    WRITTEN_OFF_AMOUNT("main.writtenOffAmount", "已核销金额", FieldTypeEnum.AMOUNT, "written_off_amount", false, false, List.of(SceneTypeEnum.LIST), null, null),
    REMAINING_AMOUNT("main.remainingAmount", "未核销余额", FieldTypeEnum.AMOUNT, "remaining_amount", false, false, List.of(SceneTypeEnum.LIST), null, null),
    STATUS("main.status", "状态", FieldTypeEnum.COMB, "status", false, false, List.of(SceneTypeEnum.LIST), "0:未核销,1:部分核销,2:已核销,3:已关闭", null),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null);

    private final String attr;
    private final String attrName;
    private final FieldTypeEnum fieldType;
    private final String filterName;
    private final boolean required;
    private final boolean editable;
    private final List<SceneTypeEnum> scenes;
    private final String options;
    private final String businessCode;

    PayableFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
        this.filterName = filterName;
        this.required = required;
        this.editable = editable;
        this.scenes = scenes;
        this.options = options;
        this.businessCode = businessCode;
    }

    public boolean supports(SceneTypeEnum scene) {
        return scenes.contains(scene);
    }

    public SceneFieldMeta toSceneFieldMeta() {
        return new SceneFieldMeta(attr, attrName, fieldType.getType(), required ? 1 : 0, editable ? 1 : 0,
            itemList(), businessCode, List.of());
    }

    public List<FieldItem> itemList() {
        if (options == null) {
            return List.of();
        }
        return Arrays.stream(options.split(",")).map(item -> item.split(":", 2)).map(item -> {
            FieldItem fieldItem = new FieldItem();
            fieldItem.setValue(item[0]);
            fieldItem.setText(item.length > 1 ? item[1] : item[0]);
            return fieldItem;
        }).toList();
    }

    public FieldEntity.BusinessSelectConfig businessSelectConfig() {
        if (businessCode == null) {
            return null;
        }
        FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
        config.setBusinessCode(businessCode);
        return config;
    }

    public static List<FieldRule> fieldRules() {
        return Arrays.stream(values()).map(field -> new FieldRule(field.attr, field.attrName,
            field.fieldType.getType(), null, field.required ? 1 : 0)).toList();
    }
}
