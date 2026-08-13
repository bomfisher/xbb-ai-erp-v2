package xbb.ai.erp.module.inventory.domain.cost;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * 每次入库后重新计算单位成本，出库按当时单位成本结转的移动加权平均策略。
 */
public final class MovingWeightedAverageCostStrategy implements CostCalculationStrategy {
    /**
     * 返回移动加权平均法的稳定编码。
     */
    @Override
    public String code() {
        return "MOVING_WEIGHTED_AVERAGE";
    }

    /**
     * 将入库金额加入余额成本，并以新的数量和金额重算单位成本。
     */
    @Override
    public CostCalculationResult inbound(CostCalculationContext context) {
        BigDecimal quantityAfter = context.quantityBefore().add(context.quantity());
        BigDecimal totalCostAfter = context.totalCostBefore().add(context.transactionCost());
        BigDecimal unitCostAfter = quantityAfter.signum() == 0 ? BigDecimal.ZERO
            : totalCostAfter.divide(quantityAfter, 6, RoundingMode.HALF_UP);
        return new CostCalculationResult(quantityAfter, context.transactionCost(), totalCostAfter, unitCostAfter);
    }

    /**
     * 按出库前移动平均单位成本结转本次出库成本。
     */
    @Override
    public CostCalculationResult outbound(CostCalculationContext context) {
        BigDecimal quantityAfter = context.quantityBefore().subtract(context.quantity());
        BigDecimal totalCostChange = context.unitCostBefore().multiply(context.quantity()).setScale(2, RoundingMode.HALF_UP).negate();
        BigDecimal totalCostAfter = context.totalCostBefore().add(totalCostChange);
        return new CostCalculationResult(quantityAfter, totalCostChange, totalCostAfter, context.unitCostBefore());
    }
}
