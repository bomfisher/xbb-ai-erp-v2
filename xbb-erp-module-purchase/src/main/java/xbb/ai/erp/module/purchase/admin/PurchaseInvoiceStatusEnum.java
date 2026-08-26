package xbb.ai.erp.module.purchase.admin;

import lombok.Getter;

@Getter
public enum PurchaseInvoiceStatusEnum {
    DRAFT("DRAFT"),
    POSTED("POSTED"),
    VOIDED("VOIDED");

    private final String code;

    PurchaseInvoiceStatusEnum(String code) {
        this.code = code;
    }
}
