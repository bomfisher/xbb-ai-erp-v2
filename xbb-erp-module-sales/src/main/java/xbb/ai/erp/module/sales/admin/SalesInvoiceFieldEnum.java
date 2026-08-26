package xbb.ai.erp.module.sales.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldRule;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.filed.ProductSelectSourceModeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum SalesInvoiceFieldEnum {
    INVOICE_NO("main.invoiceNo", "销售发票编号", FieldTypeEnum.SERIAL_NO, null, true, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    INVOICE_SOURCE("sourceSelection", "开票来源", FieldTypeEnum.INVOICE_SOURCE, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    CUSTOMER_ID("main.customerId", "客户", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "CUSTOMER", List.of()),
    INVOICE_DATE("main.invoiceDate", "发票日期", FieldTypeEnum.DATE, "invoice_date", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    DUE_DATE("main.dueDate", "应收到期日", FieldTypeEnum.DATE, "due_date", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    PAYMENT_TERM("main.paymentTerm", "付款条件", FieldTypeEnum.TEXT, "payment_term", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    UNTAXED_AMOUNT("main.untaxedAmount", "未税金额", FieldTypeEnum.AMOUNT, "untaxed_amount", false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    TAX_AMOUNT("main.taxAmount", "税额", FieldTypeEnum.AMOUNT, "tax_amount", false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    AMOUNT("main.amount", "含税总金额", FieldTypeEnum.AMOUNT, "amount", false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    INVOICE_TYPE("main.invoiceType", "发票类型", FieldTypeEnum.COMB, "invoice_type", true, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), "SALE:销售发票,CREDIT_NOTE:贷项发票", null, List.of()),
    STATUS("main.status", "状态", FieldTypeEnum.COMB, "status", false, false, List.of(SceneTypeEnum.LIST), "DRAFT:草稿,POSTED:已过账,VOIDED:已作废", null, List.of()),
    AUDIT_STATUS("main.auditStatus", "审核状态", FieldTypeEnum.COMB, "audit_status", false, false, List.of(SceneTypeEnum.LIST), "0:待审核,1:审核中,2:已审核,3:已拒绝", null, List.of()),
    AUDIT_TIME("main.auditTime", "审核时间", FieldTypeEnum.DATE, "audit_time", false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.UPDATE), null, null, List.of()),
    POSTED_TIME("main.postedTime", "过账时间", FieldTypeEnum.DATE, "posted_time", false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.UPDATE), null, null, List.of()),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT, null, false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    INVOICE_LINES("lines", "发票明细", FieldTypeEnum.SUB_ITEM, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of(
            new SceneFieldMeta("productId", "商品或服务", FieldTypeEnum.PRODUCT.getType(), 0, 1, List.of(), BusinessCodeEnum.PRODUCT_SKU.getCode(), List.of(), ProductSelectSourceModeEnum.MIXED),
            new SceneFieldMeta("productName", "商品或服务名称", FieldTypeEnum.TEXT.getType(), 1, 1, List.of(), null, List.of()),
            new SceneFieldMeta("specification", "规格型号", FieldTypeEnum.TEXT.getType(), 0, 1, List.of(), null, List.of()),
            new SceneFieldMeta("unitId", "单位", FieldTypeEnum.TEXT.getType(), 0, 1, List.of(), null, List.of()),
            new SceneFieldMeta("quantity", "数量", FieldTypeEnum.NUM_DOUBLE.getType(), 1, 1, List.of(), null, List.of()),
            new SceneFieldMeta("unitPrice", "未税单价", FieldTypeEnum.AMOUNT.getType(), 1, 1, List.of(), null, List.of()),
            new SceneFieldMeta("taxRate", "税率", FieldTypeEnum.NUM_DOUBLE.getType(), 1, 1, List.of(), null, List.of()),
            new SceneFieldMeta("untaxedAmount", "未税金额", FieldTypeEnum.AMOUNT.getType(), 0, 0, List.of(), null, List.of()),
            new SceneFieldMeta("taxAmount", "税额", FieldTypeEnum.AMOUNT.getType(), 0, 0, List.of(), null, List.of()),
            new SceneFieldMeta("amount", "含税金额", FieldTypeEnum.AMOUNT.getType(), 0, 0, List.of(), null, List.of()),
            new SceneFieldMeta("remark", "备注", FieldTypeEnum.TEXT.getType(), 0, 1, List.of(), null, List.of())
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

    SalesInvoiceFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode, List<SceneFieldMeta> subFields) {
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

    public boolean supports(SceneTypeEnum scene) {
        return scenes.contains(scene) && (fieldType != FieldTypeEnum.SUB_ITEM || scene != SceneTypeEnum.LIST);
    }

    public SceneFieldMeta toSceneFieldMeta() {
        return new SceneFieldMeta(attr, attrName, fieldType.getType(), required ? 1 : 0, editable ? 1 : 0, itemList(), businessCode, subFields);
    }

    public List<FieldItem> itemList() {
        if (options == null || options.isBlank()) return List.of();
        return Arrays.stream(options.split(",")).map(String::trim).filter(option -> !option.isEmpty()).map(option -> {
            String[] parts = option.split(":", 2);
            FieldItem item = new FieldItem();
            item.setValue(parts[0].trim());
            item.setText(parts.length == 2 ? parts[1].trim() : parts[0].trim());
            return item;
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
