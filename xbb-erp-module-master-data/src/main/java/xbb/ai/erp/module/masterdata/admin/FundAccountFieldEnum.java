package xbb.ai.erp.module.masterdata.admin;

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
public enum FundAccountFieldEnum {
    ACCOUNT_CODE("main.accountCode", "账户编码", FieldTypeEnum.TEXT, "account_code", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    ACCOUNT_NAME("main.accountName", "账户名称", FieldTypeEnum.TEXT, "account_name", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    CURRENCY("main.currency", "币别", FieldTypeEnum.COMB, "currency", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), "CNY:人民币", null, List.of()),
    BANK_ACCOUNT_NO("main.bankAccountNo", "银行账号", FieldTypeEnum.TEXT, "bank_account_no", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    ACCOUNT_HOLDER("main.accountHolder", "开户名", FieldTypeEnum.TEXT, null, false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    BANK_NAME("main.bankName", "开户行", FieldTypeEnum.TEXT, null, false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    ACCOUNT_TYPE("main.accountType", "账户类型", FieldTypeEnum.COMB, "account_type", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), "CASH:现金,BANK:银行账户,OTHER:其他", null, List.of()),
    DEFAULT_FLAG("main.defaultFlag", "默认账户", FieldTypeEnum.SWITCH, "default_flag", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    ENABLED("main.enabled", "状态", FieldTypeEnum.COMB, "enabled", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), "0:禁用,1:可用", null, List.of()),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT, null, false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
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

    FundAccountFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode, List<SceneFieldMeta> subFields) {
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
