package xbb.ai.erp.module.inventory.domain.cost;

public interface CostCalculationStrategy {
    /**
     * 返回策略的稳定编码，用于未来按配置选择策略。
     */
    String code();

    /**
     * 计算入库后的库存成本。
     */
    CostCalculationResult inbound(CostCalculationContext context);

    /**
     * 计算出库后的库存成本；调用前库存服务已完成库存量充足校验。
     */
    CostCalculationResult outbound(CostCalculationContext context);
}
