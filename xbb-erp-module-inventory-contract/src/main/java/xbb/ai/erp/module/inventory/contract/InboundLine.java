package xbb.ai.erp.module.inventory.contract;

import java.math.BigDecimal;

public record InboundLine(Long warehouseId, Long skuId, BigDecimal quantity, BigDecimal totalCost, Long sourceLineId) {
}
