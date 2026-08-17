package xbb.ai.erp.module.sales.admin;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum SalesOutboundFieldEnum {
    OUTBOUND_NO("main.outboundNo", "出库单号", FieldTypeEnum.TEXT, "outbound_no", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    SALES_ORDER_ID("main.salesOrderId", "销售订单", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "SALES_ORDER", List.of()),
    CUSTOMER_ID("main.customerId", "客户", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "CUSTOMER", List.of()),
    WAREHOUSE_ID("main.warehouseId", "出库仓库", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "WAREHOUSE", List.of()),
    OUTBOUND_DATE("main.outboundDate", "出库日期", FieldTypeEnum.DATE, "outbound_date", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    TOTAL_AMOUNT("main.totalAmount", "出库金额", FieldTypeEnum.AMOUNT, "total_amount", false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    AUDIT_STATUS("main.auditStatus", "审核状态", FieldTypeEnum.COMB, "audit_status", false, false, List.of(SceneTypeEnum.LIST), "0:待审核,1:审核中,2:已审核,3:已拒绝", null, List.of()),
    SALES_OUTBOUND_ITEM("items", "出库明细", FieldTypeEnum.SUB_ITEM, null, true, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of(new SceneFieldMeta("skuId", "sku_id", FieldTypeEnum.PRODUCT.getType(), 0, 1, List.of(), "PRODUCT_SKU", List.of()), new SceneFieldMeta("unitName", "unit_name", FieldTypeEnum.TEXT.getType(), 0, 1, List.of(), null, List.of()), new SceneFieldMeta("qty", "qty", FieldTypeEnum.NUM_DOUBLE.getType(), 0, 1, List.of(), null, List.of()), new SceneFieldMeta("unitPrice", "unit_price", FieldTypeEnum.NUM_DOUBLE.getType(), 0, 1, List.of(), null, List.of()), new SceneFieldMeta("amount", "amount", FieldTypeEnum.AMOUNT.getType(), 0, 1, List.of(), null, List.of()), new SceneFieldMeta("costUnit", "cost_unit", FieldTypeEnum.NUM_DOUBLE.getType(), 0, 1, List.of(), null, List.of()), new SceneFieldMeta("costAmount", "cost_amount", FieldTypeEnum.AMOUNT.getType(), 0, 1, List.of(), null, List.of())));

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

    SalesOutboundFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode, List<SceneFieldMeta> subFields) {
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
