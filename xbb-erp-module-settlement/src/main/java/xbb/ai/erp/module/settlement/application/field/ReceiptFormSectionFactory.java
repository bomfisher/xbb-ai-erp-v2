package xbb.ai.erp.module.settlement.application.field;

import java.util.List;
import xbb.ai.erp.base.common.filed.FormSectionEntity;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

public final class ReceiptFormSectionFactory {
    private ReceiptFormSectionFactory() {
    }

    public static List<FormSectionEntity> getSections(SceneTypeEnum scene) {
        if (scene != SceneTypeEnum.CREATE && scene != SceneTypeEnum.UPDATE) {
            return List.of();
        }
        return List.of(
            section("basic", "基本信息", 10, 2, false, List.of("main.receiptNo", "main.customerId", "main.receiptDate", "main.receiptType", "main.remark")),
            section("payment", "收款信息", 20, 1, false, List.of("payments")),
            section("write-off", "核销应收款", 30, 1, false, List.of("writeOffs"))
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
