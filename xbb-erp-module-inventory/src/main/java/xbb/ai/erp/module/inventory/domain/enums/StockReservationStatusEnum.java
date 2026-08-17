package xbb.ai.erp.module.inventory.domain.enums;

import xbb.ai.erp.base.common.exception.BizException;

public enum StockReservationStatusEnum {
    RESERVED,
    PARTIALLY_OUTBOUNDED,
    FULLY_OUTBOUNDED,
    RELEASED;

    public static StockReservationStatusEnum require(String value) {
        try {
            return valueOf(value);
        } catch (IllegalArgumentException | NullPointerException exception) {
            throw new BizException("锁库状态无效");
        }
    }

    public boolean canConsume() {
        return this == RESERVED || this == PARTIALLY_OUTBOUNDED;
    }

    public boolean canRelease() {
        return this == RESERVED || this == PARTIALLY_OUTBOUNDED;
    }
}
