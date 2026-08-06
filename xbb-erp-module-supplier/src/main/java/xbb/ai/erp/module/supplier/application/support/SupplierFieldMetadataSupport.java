package xbb.ai.erp.module.supplier.application.support;

import xbb.ai.erp.base.common.filed.FieldEntity;
import xbb.ai.erp.base.common.filed.FieldItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class SupplierFieldMetadataSupport {

    private static final List<FieldItem> SUPPLIER_CATEGORY_OPTIONS = List.of(
        option("A", "原材料供应商"),
        option("B", "服务供应商"),
        option("C", "其他供应商")
    );
    private static final List<FieldItem> MAIN_BUSINESS_CATEGORY_OPTIONS = List.of(
        option("steel", "钢材"),
        option("equipment", "设备"),
        option("service", "服务"),
        option("other", "其他")
    );
    private static final List<FieldItem> BIZ_STATUS_OPTIONS = List.of(
        option("1", "启用"),
        option("0", "停用")
    );
    private static final List<FieldItem> REF_STATUS_OPTIONS = List.of(
        option("0", "未引用"),
        option("1", "已引用")
    );

    private SupplierFieldMetadataSupport() {
    }

    public static List<FieldItem> supplierCategoryOptions() {
        return copyOptions(SUPPLIER_CATEGORY_OPTIONS);
    }

    public static List<FieldItem> mainBusinessCategoryOptions() {
        return copyOptions(MAIN_BUSINESS_CATEGORY_OPTIONS);
    }

    public static List<FieldItem> bizStatusOptions() {
        return copyOptions(BIZ_STATUS_OPTIONS);
    }

    public static List<FieldItem> refStatusOptions() {
        return copyOptions(REF_STATUS_OPTIONS);
    }

    public static FieldEntity.BusinessSelectConfig memberSingleSelectConfig(String corpid) {
        FieldEntity.BusinessSelectConfig config = new FieldEntity.BusinessSelectConfig();
        config.setBusinessType("member");
        config.setQuickSearchUrl("/erp/v1/org/memberSelect/quickSearch");
        config.setDialogSearchUrl("/erp/v1/org/memberSelect/dialogSearch");
        config.setGetByIdUrl("/erp/v1/org/memberSelect/getById");
        config.setRequestPayload(corpid == null || corpid.isBlank() ? Map.of() : Map.of("corpid", corpid));
        config.setPlaceholder("请选择成员");
        config.setDialogTitle("选择成员");
        config.setMultiple(false);
        return config;
    }

    public static String supplierCategoryText(String value) {
        return findText(SUPPLIER_CATEGORY_OPTIONS, value);
    }

    public static String mainBusinessCategoryText(String value) {
        return findText(MAIN_BUSINESS_CATEGORY_OPTIONS, value);
    }

    public static String bizStatusText(String value) {
        return findText(BIZ_STATUS_OPTIONS, value);
    }

    public static String refStatusText(String value) {
        return findText(REF_STATUS_OPTIONS, value);
    }

    private static FieldItem option(String value, String text) {
        FieldItem item = new FieldItem();
        item.setValue(value);
        item.setText(text);
        return item;
    }

    private static List<FieldItem> copyOptions(List<FieldItem> source) {
        List<FieldItem> target = new ArrayList<>(source.size());
        for (FieldItem item : source) {
            target.add(option(String.valueOf(item.getValue()), item.getText()));
        }
        return target;
    }

    private static String findText(List<FieldItem> source, String value) {
        if (value == null || value.isBlank()) {
            return value;
        }
        return source.stream()
            .filter(item -> value.equals(String.valueOf(item.getValue())))
            .map(FieldItem::getText)
            .findFirst()
            .orElse(value);
    }
}
