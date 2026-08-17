package xbb.ai.erp.module.purchase.application.field;

import java.util.List;
import xbb.ai.erp.base.common.filed.FormSectionEntity;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

public final class PurchaseOrderFormSectionFactory {
    private PurchaseOrderFormSectionFactory() {
    }

    public static List<FormSectionEntity> getSections(SceneTypeEnum scene) {
        if (scene != SceneTypeEnum.CREATE && scene != SceneTypeEnum.UPDATE) {
            return List.of();
        }
        return List.of(
            section("basic", "基本信息", 10, 4, false, List.of("main.orderNo", "main.orderDate", "main.supplierId", "main.expectedDate")),
            section("items", "产品信息", 20, 1, false, List.of("items")),
            section("amount", "金额信息", 30, 2, false, List.of("main.totalAmount")),
            section("remark", "备注信息", 40, 1, true, List.of("main.remark"))
        );
    }

    private static FormSectionEntity section(String key, String title, int order, int columns, boolean collapsed, List<String> fields) {
        FormSectionEntity section = new FormSectionEntity();
        section.setKey(key);
        section.setTitle(title);
        section.setOrder(order);
        section.setColumns(columns);
        section.setCollapsed(collapsed);
        section.setFields(fields);
        return section;
    }
}
