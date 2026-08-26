package xbb.ai.erp.module.settlement.application.field;

import java.util.List;
import xbb.ai.erp.base.common.filed.FormSectionEntity;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

public final class ReceivableFormSectionFactory {
    private ReceivableFormSectionFactory() {
    }

    public static List<FormSectionEntity> getSections(SceneTypeEnum scene) {
        if (scene != SceneTypeEnum.CREATE && scene != SceneTypeEnum.UPDATE) {
            return List.of();
        }
        return List.of(
            section("basic", "基本信息", 10, 2, false, List.of("main.receivableNo", "main.customerId", "main.sourceType", "main.sourceInvoiceId", "main.openingBatchId", "main.receivableDate", "main.dueDate", "main.amount", "main.remark"))
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
