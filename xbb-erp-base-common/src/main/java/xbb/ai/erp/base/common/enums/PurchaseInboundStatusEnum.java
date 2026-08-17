package xbb.ai.erp.base.common.enums;

import lombok.Getter;

@Getter
public enum PurchaseInboundStatusEnum {
    SUBMITTED("SUBMITTED"),
    INVENTORY_POSTED("INVENTORY_POSTED");

    private final String code;

    PurchaseInboundStatusEnum(String code) {
        this.code = code;
    }
}
