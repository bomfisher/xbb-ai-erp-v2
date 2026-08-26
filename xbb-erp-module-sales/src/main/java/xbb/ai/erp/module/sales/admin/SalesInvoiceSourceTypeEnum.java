package xbb.ai.erp.module.sales.admin;

import java.util.Arrays;

public enum SalesInvoiceSourceTypeEnum {
    SALES_OUTBOUND,
    SALES_ORDER,
    MANUAL;

    public static boolean isDocumentSource(String sourceType) {
        return Arrays.stream(values()).anyMatch(item -> item.name().equals(sourceType) && item != MANUAL);
    }
}
