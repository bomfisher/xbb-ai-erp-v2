package xbb.ai.erp.module.purchase.admin;

public enum PurchaseInvoiceSourceTypeEnum {
    PURCHASE_INBOUND,
    PURCHASE_ORDER,
    MANUAL;

    public static boolean isSupported(String value) {
        for (PurchaseInvoiceSourceTypeEnum type : values()) {
            if (type.name().equals(value)) {
                return true;
            }
        }
        return false;
    }
}
