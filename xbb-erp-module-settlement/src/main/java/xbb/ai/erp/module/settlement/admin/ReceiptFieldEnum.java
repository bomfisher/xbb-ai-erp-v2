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
public enum ReceiptFieldEnum {
    RECEIPT_NO("main.receiptNo", "收款单编号", FieldTypeEnum.SERIAL_NO, null, true, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    CUSTOMER_ID("main.customerId", "客户", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "CUSTOMER", List.of()),
    RECEIPT_DATE("main.receiptDate", "收款日期", FieldTypeEnum.DATE, "receipt_date", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    AMOUNT("main.amount", "实际收款金额", FieldTypeEnum.AMOUNT, "amount", false, false, List.of(SceneTypeEnum.LIST), null, null, List.of()),
    WRITTEN_OFF_AMOUNT("main.writtenOffAmount", "已核销金额", FieldTypeEnum.AMOUNT, "written_off_amount", false, false, List.of(SceneTypeEnum.LIST), null, null, List.of()),
    REMAINING_AMOUNT("main.remainingAmount", "未核销收款余额", FieldTypeEnum.AMOUNT, "remaining_amount", false, false, List.of(SceneTypeEnum.LIST), null, null, List.of()),
    RECEIPT_TYPE("main.receiptType", "收款类型", FieldTypeEnum.COMB, "receipt_type", true, true, List.of(), "CUSTOMER_PAYMENT:普通收款,ADVANCE_PAYMENT:预收,OPENING_BALANCE:期初收款", null, List.of()),
    PAYMENT_METHOD("main.paymentMethod", "收款方式", FieldTypeEnum.COMB, "payment_method", false, false, List.of(SceneTypeEnum.LIST), "COMMERCIAL_ACCEPTANCE:商业承兑汇票,BANK_ACCEPTANCE:银行承兑汇票,WECHAT_PAY:微信支付,ALIPAY:支付宝支付,BANK_TRANSFER:银行转账,BANK_CHECK:银行支票,CASH:现金支付", null, List.of()),
    PAYMENTS("payments", "收款信息", FieldTypeEnum.SUB_ITEM, null, true, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of(
        new SceneFieldMeta("bankAccountId", "收款账户", FieldTypeEnum.FUND_ACCOUNT.getType(), 1, 1, List.of(), "FUND_ACCOUNT", List.of()),
        new SceneFieldMeta("paymentMethod", "收款方式", FieldTypeEnum.COMB.getType(), 1, 1, paymentMethodOptions(), null, List.of()),
        new SceneFieldMeta("amount", "收款金额", FieldTypeEnum.AMOUNT.getType(), 1, 1),
        new SceneFieldMeta("handlingFee", "手续费", FieldTypeEnum.AMOUNT.getType(), 0, 1),
        new SceneFieldMeta("transactionNo", "交易号/票据号", FieldTypeEnum.TEXT.getType(), 0, 1),
        new SceneFieldMeta("remark", "收款备注", FieldTypeEnum.TEXT.getType(), 0, 1)
    )),
    WRITE_OFFS("writeOffs", "核销应收款", FieldTypeEnum.SUB_ITEM, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of(
        new SceneFieldMeta("receivableId", "应收款", FieldTypeEnum.BUSINESS.getType(), 1, 1, List.of(), "RECEIVABLE", List.of()),
        new SceneFieldMeta("amount", "核销金额", FieldTypeEnum.AMOUNT.getType(), 1, 1)
    )),
    STATUS("main.status", "核销状态", FieldTypeEnum.COMB, "订单状态", false, false, List.of(SceneTypeEnum.LIST), "0:未核销,1:部分核销,2:已核销,3:已关闭", null, List.of()),
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

    ReceiptFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode, List<SceneFieldMeta> subFields) {
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

    private static List<FieldItem> paymentMethodOptions() {
        return PAYMENT_METHOD.itemList();
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
