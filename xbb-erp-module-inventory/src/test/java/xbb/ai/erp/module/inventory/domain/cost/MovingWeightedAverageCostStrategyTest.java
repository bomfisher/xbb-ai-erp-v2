package xbb.ai.erp.module.inventory.domain.cost;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class MovingWeightedAverageCostStrategyTest {

    private final MovingWeightedAverageCostStrategy strategy = new MovingWeightedAverageCostStrategy();

    @Test
    void outboundShouldClearUnitCostWhenQuantityBecomesZero() {
        CostCalculationResult result = strategy.outbound(new CostCalculationContext(
            new BigDecimal("10"), BigDecimal.ZERO, new BigDecimal("10"), new BigDecimal("40"), new BigDecimal("4")
        ));

        assertEquals(BigDecimal.ZERO, result.quantityAfter());
        assertEquals(0, result.totalCostAfter().compareTo(BigDecimal.ZERO));
        assertEquals(BigDecimal.ZERO, result.unitCostAfter());
    }

    @Test
    void outboundShouldKeepUnitCostWhenQuantityRemains() {
        CostCalculationResult result = strategy.outbound(new CostCalculationContext(
            new BigDecimal("4"), BigDecimal.ZERO, new BigDecimal("10"), new BigDecimal("40"), new BigDecimal("4")
        ));

        assertEquals(new BigDecimal("6"), result.quantityAfter());
        assertEquals(new BigDecimal("4"), result.unitCostAfter());
    }
}
