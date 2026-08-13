package xbb.ai.erp.module.inventory.contract;

import java.math.BigDecimal;

/**
 * 调用方提供的出库业务事实；成本由库存模块计算，调用方不得传入出库成本。
 */
public record OutboundLine(Long warehouseId, Long skuId, BigDecimal quantity, Long sourceLineId) {
}
