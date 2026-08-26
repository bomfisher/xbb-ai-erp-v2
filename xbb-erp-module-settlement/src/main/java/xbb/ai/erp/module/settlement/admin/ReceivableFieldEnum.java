package xbb.ai.erp.module.settlement.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldRule;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum ReceivableFieldEnum {
    RECEIVABLE_NO("main.receivableNo", "应收开放项编号", FieldTypeEnum.SERIAL_NO, null, true, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    CUSTOMER_ID("main.customerId", "客户", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "CUSTOMER", List.of()),
    SOURCE_TYPE("main.sourceType", "来源类型", FieldTypeEnum.COMB, "source_type", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), "SALES_INVOICE:销售发票,OPENING_BALANCE:期初余额,MANUAL_ADJUSTMENT:手工调整", null, List.of()),
    SOURCE_INVOICE_ID("main.sourceInvoiceId", "来源销售发票", FieldTypeEnum.BUSINESS, null, false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "SALES_INVOICE", List.of()),
    OPENING_BATCH_ID("main.openingBatchId", "期初导入批次", FieldTypeEnum.NUM_INT, null, false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    RECEIVABLE_DATE("main.receivableDate", "应收确认日期", FieldTypeEnum.DATE, "receivable_date", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    DUE_DATE("main.dueDate", "到期日", FieldTypeEnum.DATE, "due_date", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    AMOUNT("main.amount", "初始应收金额", FieldTypeEnum.AMOUNT, "amount", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    WRITTEN_OFF_AMOUNT("main.writtenOffAmount", "已核销金额", FieldTypeEnum.AMOUNT, "written_off_amount", false, false, List.of(SceneTypeEnum.LIST), null, null, List.of()),
    REMAINING_AMOUNT("main.remainingAmount", "未核销应收余额", FieldTypeEnum.AMOUNT, "remaining_amount", false, false, List.of(SceneTypeEnum.LIST), null, null, List.of()),
    STATUS("main.status", "核销状态", FieldTypeEnum.COMB, "status", false, false, List.of(SceneTypeEnum.LIST), "0:未核销,1:部分核销,2:已核销,3:已关闭", null, List.of()),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    CREATOR_ID("main.creatorId", "创建人", FieldTypeEnum.USER, "creator_id", false, false, List.of(SceneTypeEnum.LIST), null, "ORG_MEMBER", List.of()),
    MODIFY_ID("main.modifyId", "修改人", FieldTypeEnum.USER, "modify_id", false, false, List.of(SceneTypeEnum.LIST), null, "ORG_MEMBER", List.of());

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

    ReceivableFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode, List<SceneFieldMeta> subFields) {
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

    public static List<FieldRule> fieldRules() {
        return Arrays.stream(values())
            .flatMap(field -> field.fieldType == FieldTypeEnum.SUB_ITEM
                ? Stream.concat(
                    Stream.of(fieldRule(field.attr, field.attrName, field.fieldType.getType(), field.required)),
                    field.subFields.stream().map(subField -> fieldRule(
                        field.attr + "." + subField.getAttr(), subField.getAttrName(), subField.getFieldType(),
                        Integer.valueOf(1).equals(subField.getRequired())
                    ))
                )
                : Stream.of(fieldRule(field.attr, field.attrName, field.fieldType.getType(), field.required)))
            .toList();
    }

    private static FieldRule fieldRule(String attr, String attrName, Integer fieldType, boolean required) {
        return new FieldRule(attr, attrName, fieldType, null, required ? 1 : 0);
    }

    public FieldEntity.BusinessSelectConfig businessSelectConfig() {
        if (businessCode == null || businessCode.isBlank()) return null;
        FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
        config.setBusinessCode(businessCode);
        return config;
    }
}
