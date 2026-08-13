package xbb.ai.erp.module.inventory.domain.cost;

import java.math.BigDecimal;

/**
 * 成本策略执行一次库存数量变动所需的只读快照。
 */
public record CostCalculationContext(BigDecimal quantity, BigDecimal transactionCost, BigDecimal quantityBefore,
                                     BigDecimal totalCostBefore, BigDecimal unitCostBefore) {
}
