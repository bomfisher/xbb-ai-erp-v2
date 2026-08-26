package xbb.ai.erp.module.inventory.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.inventory.domain.model.StockBalance;
import xbb.ai.erp.module.inventory.domain.repository.StockBalanceRepository;

class InventoryStockQueryServiceTest {
    @Test
    void shouldSumSkuBalancesWhenWarehouseIsNotSelected() {
        StockBalanceRepository stockBalanceRepository = mock(StockBalanceRepository.class);
        InventoryStockQueryService inventoryStockQueryService = new InventoryStockQueryService(stockBalanceRepository);
        when(stockBalanceRepository.findBySku("corp-001", 1001L)).thenReturn(List.of(balance("12.50"), balance("3.50")));

        BigDecimal result = inventoryStockQueryService.queryInstantQty("corp-001", 1001L, null);

        assertEquals(0, result.compareTo(new BigDecimal("16.00")));
    }

    @Test
    void shouldReturnWarehouseBalanceWhenWarehouseIsSelected() {
        StockBalanceRepository stockBalanceRepository = mock(StockBalanceRepository.class);
        InventoryStockQueryService inventoryStockQueryService = new InventoryStockQueryService(stockBalanceRepository);
        when(stockBalanceRepository.findByWarehouseAndSku("corp-001", 2001L, 1001L)).thenReturn(balance("8"));

        BigDecimal result = inventoryStockQueryService.queryInstantQty("corp-001", 1001L, 2001L);

        assertEquals(0, result.compareTo(new BigDecimal("8")));
    }

    private StockBalance balance(String quantity) {
        StockBalance stockBalance = new StockBalance();
        stockBalance.setQty(new BigDecimal(quantity));
        return stockBalance;
    }
}
