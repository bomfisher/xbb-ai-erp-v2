package xbb.ai.erp.module.purchase.application.field;

import java.util.List;
import xbb.ai.erp.base.common.filed.FormSectionEntity;
import xbb.ai.erp.scene.meta.SceneTypeEnum;

public final class PurchaseInvoiceFormSectionFactory {

    private PurchaseInvoiceFormSectionFactory() {
    }

    public static List<FormSectionEntity> getSections(SceneTypeEnum scene) {
        if (scene != SceneTypeEnum.CREATE && scene != SceneTypeEnum.UPDATE) {
            return List.of();
        }
        return List.of(
            section("basic", "基本信息", 10, 2, false, List.of(
                "main.invoiceNo", "main.supplierId", "main.supplierInvoiceNo", "main.invoiceDate",
                "main.dueDate", "main.paymentTerm", "main.invoiceType", "main.remark")),
            section("source", "开票来源", 20, 1, false, List.of("sourceSelection")),
            section("lines", "发票明细", 30, 2, false, List.of("lines")),
            section("amount", "金额汇总", 40, 2, false, List.of(
                "main.untaxedAmount", "main.taxAmount", "main.amount"))
        );
    }

    private static FormSectionEntity section(String key, String title, int order, int columns,
        boolean collapsed, List<String> fields) {
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
