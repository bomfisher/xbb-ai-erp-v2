package xbb.ai.erp.module.settlement.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldRule;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum PaymentFieldEnum {
    PAYMENT_NO("main.paymentNo", "付款单编号", FieldTypeEnum.SERIAL_NO, null, true, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    SUPPLIER_ID("main.supplierId", "供应商", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "SUPPLIER"),
    PAYMENT_DATE("main.paymentDate", "付款日期", FieldTypeEnum.DATE, "payment_date", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    AMOUNT("main.amount", "实际付款金额", FieldTypeEnum.AMOUNT, "amount", false, false, List.of(SceneTypeEnum.LIST), null, null),
    WRITTEN_OFF_AMOUNT("main.writtenOffAmount", "已核销金额", FieldTypeEnum.AMOUNT, "written_off_amount", false, false, List.of(SceneTypeEnum.LIST), null, null),
    REMAINING_AMOUNT("main.remainingAmount", "未核销付款余额", FieldTypeEnum.AMOUNT, "remaining_amount", false, false, List.of(SceneTypeEnum.LIST), null, null),
    STATUS("main.status", "核销状态", FieldTypeEnum.COMB, "status", false, false, List.of(SceneTypeEnum.LIST), "0:未核销,1:部分核销,2:已核销,3:已关闭", null),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    PAYMENT_INFOS("paymentInfos", "付款信息", FieldTypeEnum.SUB_ITEM, null, true, true,
        List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of(
            new SceneFieldMeta("bankAccountId", "付款账户", FieldTypeEnum.FUND_ACCOUNT.getType(), 1, 1,
                List.of(), "FUND_ACCOUNT", List.of()),
            new SceneFieldMeta("paymentMethod", "付款方式", FieldTypeEnum.COMB.getType(), 1, 1,
                paymentMethodOptions(), null, List.of()),
            new SceneFieldMeta("amount", "付款金额", FieldTypeEnum.AMOUNT.getType(), 1, 1),
            new SceneFieldMeta("transactionNo", "对接流水号", FieldTypeEnum.TEXT.getType(), 0, 1),
            new SceneFieldMeta("remark", "付款备注", FieldTypeEnum.TEXT.getType(), 0, 1)
        )),
    WRITE_OFFS("writeOffs", "核销应付款", FieldTypeEnum.SUB_ITEM, null, false, true,
        List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of(
            new SceneFieldMeta("payableId", "应付款", FieldTypeEnum.BUSINESS.getType(), 1, 1,
                List.of(), "PAYABLE", List.of()),
            new SceneFieldMeta("amount", "核销金额", FieldTypeEnum.AMOUNT.getType(), 1, 1)
        ));

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

    PaymentFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required,
        boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode) {
        this(attr, attrName, fieldType, filterName, required, editable, scenes, options, businessCode, List.of());
    }

    PaymentFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required,
        boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode,
        List<SceneFieldMeta> subFields) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
        this.filterName = filterName;
        this.required = required;
        this.editable = editable;
        this.scenes = scenes;
        this.options = options;
        this.businessCode = businessCode;
        this.subFields = subFields;
    }

    public boolean supports(SceneTypeEnum scene) { return scenes.contains(scene); }

    public SceneFieldMeta toSceneFieldMeta() {
        return new SceneFieldMeta(attr, attrName, fieldType.getType(), required ? 1 : 0, editable ? 1 : 0,
            itemList(), businessCode, subFields);
    }

    public List<FieldItem> itemList() {
        if (options == null) return List.of();
        return Arrays.stream(options.split(",")).map(item -> item.split(":", 2)).map(item -> {
            FieldItem fieldItem = new FieldItem();
            fieldItem.setValue(item[0]);
            fieldItem.setText(item.length > 1 ? item[1] : item[0]);
            return fieldItem;
        }).toList();
    }

    private static List<FieldItem> paymentMethodOptions() {
        return Arrays.stream("BANK_TRANSFER:银行转账,WECHAT_PAY:微信支付,ALIPAY:支付宝,CASH:现金支付".split(","))
            .map(item -> item.split(":", 2))
            .map(item -> {
                FieldItem fieldItem = new FieldItem();
                fieldItem.setValue(item[0]);
                fieldItem.setText(item[1]);
                return fieldItem;
            }).toList();
    }

    public FieldEntity.BusinessSelectConfig businessSelectConfig() {
        if (businessCode == null) return null;
        FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
        config.setBusinessCode(businessCode);
        return config;
    }

    public static List<FieldRule> fieldRules() {
        return Arrays.stream(values()).flatMap(field -> {
            Stream<FieldRule> mainRule = Stream.of(new FieldRule(field.attr, field.attrName,
                field.fieldType.getType(), null, field.required ? 1 : 0));
            if (field.fieldType != FieldTypeEnum.SUB_ITEM) {
                return mainRule;
            }
            return Stream.concat(mainRule, field.subFields.stream().map(subField -> new FieldRule(
                field.attr + "." + subField.getAttr(), subField.getAttrName(), subField.getFieldType(), null,
                Integer.valueOf(1).equals(subField.getRequired()) ? 1 : 0)));
        }).toList();
    }
}
