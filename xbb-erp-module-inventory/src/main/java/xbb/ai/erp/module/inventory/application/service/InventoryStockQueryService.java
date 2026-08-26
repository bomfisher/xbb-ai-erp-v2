package xbb.ai.erp.module.inventory.application.service;

import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.inventory.contract.InventoryStockQueryApi;
import xbb.ai.erp.module.inventory.domain.model.StockBalance;
import xbb.ai.erp.module.inventory.domain.repository.StockBalanceRepository;

@Service
@RequiredArgsConstructor
public class InventoryStockQueryService implements InventoryStockQueryApi {
    private final StockBalanceRepository stockBalanceRepository;

    @Override
    public BigDecimal queryInstantQty(String corpid, Long skuId, Long warehouseId) {
        if (corpid == null || corpid.isBlank() || skuId == null) {
            return BigDecimal.ZERO;
        }
        if (warehouseId != null) {
            StockBalance balance = stockBalanceRepository.findByWarehouseAndSku(corpid, warehouseId, skuId);
            return value(balance == null ? null : balance.getQty());
        }
        List<StockBalance> balances = stockBalanceRepository.findBySku(corpid, skuId);
        return balances.stream().map(StockBalance::getQty).map(this::value).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private BigDecimal value(BigDecimal quantity) {
        return quantity == null ? BigDecimal.ZERO : quantity;
    }
}
