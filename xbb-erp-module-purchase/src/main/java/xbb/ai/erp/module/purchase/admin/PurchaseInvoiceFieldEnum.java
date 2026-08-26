package xbb.ai.erp.module.purchase.admin;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldRule;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;

@Getter
public enum PurchaseInvoiceFieldEnum {
    INVOICE_NO("main.invoiceNo", "采购发票编号", FieldTypeEnum.SERIAL_NO, null, true, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    INVOICE_SOURCE("sourceSelection", "开票来源", FieldTypeEnum.INVOICE_SOURCE, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    SUPPLIER_ID("main.supplierId", "供应商", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "SUPPLIER"),
    SUPPLIER_INVOICE_NO("main.supplierInvoiceNo", "供应商发票号码", FieldTypeEnum.TEXT, "supplier_invoice_no", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    INVOICE_DATE("main.invoiceDate", "发票日期", FieldTypeEnum.DATE, "invoice_date", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    DUE_DATE("main.dueDate", "应付到期日", FieldTypeEnum.DATE, "due_date", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    PAYMENT_TERM("main.paymentTerm", "付款条件", FieldTypeEnum.TEXT, "payment_term", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    UNTAXED_AMOUNT("main.untaxedAmount", "未税金额", FieldTypeEnum.AMOUNT, "untaxed_amount", false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    TAX_AMOUNT("main.taxAmount", "税额", FieldTypeEnum.AMOUNT, "tax_amount", false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    AMOUNT("main.amount", "含税总金额", FieldTypeEnum.AMOUNT, "amount", false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    INVOICE_TYPE("main.invoiceType", "发票类型", FieldTypeEnum.COMB, "invoice_type", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), "PURCHASE:采购发票,CREDIT_NOTE:采购红字发票", null),
    STATUS("main.status", "状态", FieldTypeEnum.COMB, "status", false, false, List.of(SceneTypeEnum.LIST), "DRAFT:草稿,POSTED:已过账,VOIDED:已作废", null),
    AUDIT_STATUS("main.auditStatus", "审核状态", FieldTypeEnum.COMB, "audit_status", false, false, List.of(SceneTypeEnum.LIST), "0:待审核,1:审核中,2:已审核,3:已拒绝", null),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null),
    INVOICE_LINES("lines", "发票明细", FieldTypeEnum.SUB_ITEM, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null,
        List.of(
            new SceneFieldMeta("productId", "商品或服务", FieldTypeEnum.PRODUCT.getType(), 0, 1, List.of(), BusinessCodeEnum.PRODUCT_SKU.getCode(), List.of()),
            new SceneFieldMeta("productName", "商品或服务名称", FieldTypeEnum.TEXT.getType(), 1, 1),
            new SceneFieldMeta("specification", "规格型号", FieldTypeEnum.TEXT.getType(), 0, 1),
            new SceneFieldMeta("unitName", "单位", FieldTypeEnum.TEXT.getType(), 0, 1),
            new SceneFieldMeta("quantity", "数量", FieldTypeEnum.NUM_DOUBLE.getType(), 1, 1),
            new SceneFieldMeta("unitPrice", "未税单价", FieldTypeEnum.AMOUNT.getType(), 1, 1),
            new SceneFieldMeta("taxRate", "税率", FieldTypeEnum.NUM_DOUBLE.getType(), 1, 1),
            new SceneFieldMeta("untaxedAmount", "未税金额", FieldTypeEnum.AMOUNT.getType(), 0, 0),
            new SceneFieldMeta("taxAmount", "税额", FieldTypeEnum.AMOUNT.getType(), 0, 0),
            new SceneFieldMeta("amount", "含税金额", FieldTypeEnum.AMOUNT.getType(), 0, 0),
            new SceneFieldMeta("remark", "备注", FieldTypeEnum.TEXT.getType(), 0, 1)
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

    PurchaseInvoiceFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName,
            boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode) {
        this.attr = attr;
        this.attrName = attrName;
        this.fieldType = fieldType;
        this.filterName = filterName;
        this.required = required;
        this.editable = editable;
        this.scenes = scenes;
        this.options = options;
        this.businessCode = businessCode;
        this.subFields = List.of();
    }

    PurchaseInvoiceFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName,
        boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode,
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

    public boolean supports(SceneTypeEnum scene) {
        return scenes.contains(scene);
    }

    public SceneFieldMeta toSceneFieldMeta() {
        return new SceneFieldMeta(attr, attrName, fieldType.getType(), required ? 1 : 0, editable ? 1 : 0,
            itemList(), businessCode, subFields);
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
        return Arrays.stream(values()).flatMap(field -> field.fieldType == FieldTypeEnum.SUB_ITEM
            ? java.util.stream.Stream.concat(java.util.stream.Stream.of(new FieldRule(field.attr, field.attrName,
                field.fieldType.getType(), null, field.required ? 1 : 0)), field.subFields.stream().map(sub -> new FieldRule(
                field.attr + "." + sub.getAttr(), sub.getAttrName(), sub.getFieldType(), null,
                Integer.valueOf(1).equals(sub.getRequired()) ? 1 : 0)))
            : java.util.stream.Stream.of(new FieldRule(field.attr, field.attrName, field.fieldType.getType(), null,
                field.required ? 1 : 0))).toList();
    }
}
