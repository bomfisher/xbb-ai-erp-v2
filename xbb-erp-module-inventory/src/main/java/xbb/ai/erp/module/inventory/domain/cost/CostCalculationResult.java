package xbb.ai.erp.module.inventory.domain.cost;

import java.math.BigDecimal;

/**
 * 成本策略计算出的库存余额和成本变化结果。
 */
public record CostCalculationResult(BigDecimal quantityAfter, BigDecimal totalCostChange,
                                    BigDecimal totalCostAfter, BigDecimal unitCostAfter) {
}
