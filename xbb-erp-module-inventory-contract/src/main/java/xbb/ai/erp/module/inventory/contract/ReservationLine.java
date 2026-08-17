package xbb.ai.erp.module.inventory.contract;

import java.math.BigDecimal;

public record ReservationLine(Long warehouseId, Long skuId, BigDecimal quantity, Long sourceLineId) {
}
