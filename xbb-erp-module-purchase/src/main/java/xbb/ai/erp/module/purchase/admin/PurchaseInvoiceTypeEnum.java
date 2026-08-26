package xbb.ai.erp.module.purchase.admin;

import lombok.Getter;

@Getter
public enum PurchaseInvoiceTypeEnum {
    PURCHASE("PURCHASE"),
    CREDIT_NOTE("CREDIT_NOTE");

    private final String code;

    PurchaseInvoiceTypeEnum(String code) {
        this.code = code;
    }
}
