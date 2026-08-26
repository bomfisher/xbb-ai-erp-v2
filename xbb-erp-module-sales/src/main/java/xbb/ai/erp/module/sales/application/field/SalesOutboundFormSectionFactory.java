package xbb.ai.erp.module.sales.application.field;

import java.util.List;
import xbb.ai.erp.base.common.filed.FormSectionEntity;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

public final class SalesOutboundFormSectionFactory {
    private SalesOutboundFormSectionFactory() {
    }

    public static List<FormSectionEntity> getSections(SceneTypeEnum scene) {
        if (scene != SceneTypeEnum.CREATE && scene != SceneTypeEnum.UPDATE) {
            return List.of();
        }
        return List.of(
            section("basic", "基本信息", 10, 4, false, List.of("main.outboundNo", "main.salesOrderId", "main.customerId", "main.warehouseId", "main.outboundDate")),
            section("items", "产品信息", 20, 1, false, List.of("items")),
            section("amount", "金额信息", 30, 2, false, List.of("main.totalAmount"))
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
