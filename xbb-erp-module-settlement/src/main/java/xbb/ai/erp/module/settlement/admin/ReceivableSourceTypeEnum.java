package xbb.ai.erp.module.settlement.admin;

import lombok.Getter;

@Getter
public enum ReceivableSourceTypeEnum {
    SALES_INVOICE("SALES_INVOICE"),
    OPENING_BALANCE("OPENING_BALANCE"),
    MANUAL_ADJUSTMENT("MANUAL_ADJUSTMENT");

    private final String code;

    ReceivableSourceTypeEnum(String code) {
        this.code = code;
    }
}
