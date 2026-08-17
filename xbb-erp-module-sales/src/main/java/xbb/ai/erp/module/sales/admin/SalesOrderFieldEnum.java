package xbb.ai.erp.module.sales.admin;

import java.util.Arrays;
import java.util.List;
import lombok.Getter;
import xbb.ai.erp.base.common.filed.FieldItem;
import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldTypeEnum;
import xbb.ai.erp.base.common.enums.DocumentStatusEnum;
import xbb.ai.erp.base.common.enums.StatusOptionEnum;
import xbb.ai.erp.scene.meta.SceneFieldMeta;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

@Getter
public enum SalesOrderFieldEnum {
    ORDER_NO("main.orderNo", "订单编号", FieldTypeEnum.TEXT, "order_no", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    CUSTOMER_ID("main.customerId", "客户", FieldTypeEnum.BUSINESS, null, true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "CUSTOMER", List.of()),
    WAREHOUSE_ID("main.warehouseId", "快捷选择仓库", FieldTypeEnum.BUSINESS, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, "WAREHOUSE", List.of()),
    ORDER_DATE("main.orderDate", "下单日期", FieldTypeEnum.DATE, "order_date", true, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    DELIVERY_DATE("main.deliveryDate", "交货日期", FieldTypeEnum.DATE, "delivery_date", false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    TOTAL_AMOUNT("main.totalAmount", "订单金额", FieldTypeEnum.AMOUNT, "total_amount", false, false, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    STATUS("main.status", "单据状态", FieldTypeEnum.COMB, "status", false, false, List.of(SceneTypeEnum.LIST), StatusOptionEnum.options(DocumentStatusEnum.values()), null, List.of()),
    REMARK("main.remark", "备注", FieldTypeEnum.TEXT, null, false, true, List.of(SceneTypeEnum.LIST, SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of()),
    CREATOR_ID("main.creatorId", "创建人", FieldTypeEnum.USER, "creator_id", false, false, List.of(SceneTypeEnum.LIST), null, "ORG_MEMBER", List.of()),
    MODIFY_ID("main.modifyId", "修改人", FieldTypeEnum.USER, "modify_id", false, false, List.of(SceneTypeEnum.LIST), null, "ORG_MEMBER", List.of()),
    AUDIT_STATUS("main.auditStatus", "审核状态", FieldTypeEnum.COMB, "audit_status", false, false, List.of(SceneTypeEnum.LIST), "0:待审核,1:审核中,2:已审核,3:已拒绝", null, List.of()),
    OUTBOUND_STATUS("main.outboundStatus", "出库状态", FieldTypeEnum.COMB, "outbound_status", false, false, List.of(SceneTypeEnum.LIST), "0:未出库,1:部分出库,2:全部出库", null, List.of()),
    RECEIPT_STATUS("main.receiptStatus", "收款状态", FieldTypeEnum.COMB, "receipt_status", false, false, List.of(SceneTypeEnum.LIST), "0:未收款,1:部分收款,2:全部收款", null, List.of()),
    SALES_ORDER_ITEM("items", "销售订单行", FieldTypeEnum.SUB_ITEM, null, false, true, List.of(SceneTypeEnum.CREATE, SceneTypeEnum.UPDATE), null, null, List.of(new SceneFieldMeta("skuId", "商品", FieldTypeEnum.PRODUCT.getType(), 1, 1, List.of(), "PRODUCT_SKU", List.of()), new SceneFieldMeta("warehouseId", "锁库仓库", FieldTypeEnum.BUSINESS.getType(), 0, 1, List.of(), "WAREHOUSE", List.of()), new SceneFieldMeta("specification", "规格", FieldTypeEnum.TEXT.getType(), 0, 1, List.of(), null, List.of()), new SceneFieldMeta("unitName", "单位", FieldTypeEnum.TEXT.getType(), 1, 1, List.of(), null, List.of()), new SceneFieldMeta("qty", "数量", FieldTypeEnum.NUM_DOUBLE.getType(), 1, 1, List.of(), null, List.of()), new SceneFieldMeta("deliveredQty", "已出库数量", FieldTypeEnum.NUM_DOUBLE.getType(), 0, 0, List.of(), null, List.of()), new SceneFieldMeta("unitPrice", "单价", FieldTypeEnum.NUM_DOUBLE.getType(), 1, 1, List.of(), null, List.of()), new SceneFieldMeta("taxRate", "税率", FieldTypeEnum.NUM_DOUBLE.getType(), 0, 1, List.of(), null, List.of()), new SceneFieldMeta("amount", "金额", FieldTypeEnum.AMOUNT.getType(), 1, 0, List.of(), null, List.of())));

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

    SalesOrderFieldEnum(String attr, String attrName, FieldTypeEnum fieldType, String filterName, boolean required, boolean editable, List<SceneTypeEnum> scenes, String options, String businessCode, List<SceneFieldMeta> subFields) {
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
