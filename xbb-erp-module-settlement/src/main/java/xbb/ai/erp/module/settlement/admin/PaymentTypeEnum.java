package xbb.ai.erp.module.settlement.admin;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum PaymentTypeEnum {
    SUPPLIER_PAYMENT("SUPPLIER_PAYMENT"),
    ADVANCE_PAYMENT("ADVANCE_PAYMENT"),
    OPENING_BALANCE("OPENING_BALANCE");

    private final String code;
}
