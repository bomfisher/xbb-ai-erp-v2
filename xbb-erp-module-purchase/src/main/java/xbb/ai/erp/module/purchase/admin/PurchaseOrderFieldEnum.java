package xbb.ai.erp.module.purchase.admin;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

import lombok.Getter;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.enums.InboundStatusEnum;
import xbb.ai.erp.base.common.enums.InvoiceStatusEnum;
import xbb.ai.erp.base.common.enums.PaymentStatusEnum;
import xbb.ai.erp.base.common.enums.StatusOptionEnum;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldRule;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.filed.ProductSelectSourceModeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum PurchaseOrderFieldEnum {
    ORDER_NO("main.orderNo", "采购编号", FieldTypeEnum.SERIAL_NO, "order_no", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    SUPPLIER_ID("main.supplierId", "供应商", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "SUPPLIER", List.of()),
    ORDER_DATE("main.orderDate", "单据日期", FieldTypeEnum.DATE, "order_date", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    EXPECTED_DATE("main.expectedDate", "结算期限", FieldTypeEnum.DATE, "expected_date", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    TOTAL_AMOUNT("main.totalAmount", "采购金额", FieldTypeEnum.AMOUNT, "total_amount", false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    STATUS("main.status", "status", FieldTypeEnum.COMB, "订单状态", false, true, List.of(SceneTypeEnum.LIST), "1:启用,0:禁用", null, List.of()),
    AUDIT_STATUS("main.auditStatus", "审批状态", FieldTypeEnum.COMB, "audit_status", false, false, List.of(SceneTypeEnum.LIST), StatusOptionEnum.options(AuditStatusEnum.values()), null, List.of()),
    INBOUND_STATUS("main.inboundStatus", "入库状态", FieldTypeEnum.COMB, "inbound_status", false, false, List.of(SceneTypeEnum.LIST), StatusOptionEnum.options(InboundStatusEnum.values()), null, List.of()),
    PAYMENT_STATUS("main.paymentStatus", "付款状态", FieldTypeEnum.COMB, "payment_status", false, false, List.of(SceneTypeEnum.LIST), StatusOptionEnum.options(PaymentStatusEnum.values()), null, List.of()),
    INVOICE_STATUS("main.invoiceStatus", "开票状态", FieldTypeEnum.COMB, "invoice_status", false, false, List.of(SceneTypeEnum.LIST), StatusOptionEnum.options(InvoiceStatusEnum.values()), null, List.of()),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT, "remark", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    ITEMS("items", "采购产品", FieldTypeEnum.SUB_ITEM, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of(
        new SceneFieldMeta("skuId", "产品", FieldTypeEnum.PRODUCT.getType(), 1, 1, List.of(), "PRODUCT_SKU", List.of(), ProductSelectSourceModeEnum.MASTER_ONLY),
        new SceneFieldMeta("warehouseId", "仓库", FieldTypeEnum.BUSINESS.getType(), 0, 1, List.of(), "WAREHOUSE", List.of()),
        new SceneFieldMeta("currentStock", "当前库存数量", FieldTypeEnum.STOCK.getType(), 0, 0),
        new SceneFieldMeta("unitName", "单位", FieldTypeEnum.TEXT.getType(), 0, 0),
        new SceneFieldMeta("qty", "数量", FieldTypeEnum.NUM_DOUBLE.getType(), 1, 1),
        new SceneFieldMeta("unitPrice", "单价", FieldTypeEnum.AMOUNT.getType(), 1, 1))),
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
}
