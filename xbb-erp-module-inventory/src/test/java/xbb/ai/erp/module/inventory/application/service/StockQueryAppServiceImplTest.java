package xbb.ai.erp.module.inventory.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.inventory.admin.dto.StockQueryDTO;
import xbb.ai.erp.module.inventory.application.service.impl.StockQueryAppServiceImpl;
import xbb.ai.erp.module.inventory.domain.pojo.StockBalanceQueryPojo;
import xbb.ai.erp.module.inventory.domain.repository.StockBalanceRepository;
import xbb.ai.erp.module.masterdata.contract.InventoryMasterDataQueryApi;

class StockQueryAppServiceImplTest {
    @Test
    void shouldBatchRenderProductAndWarehouseReferences() {
        StockBalanceRepository repository = mock(StockBalanceRepository.class);
        InventoryMasterDataQueryApi masterData = mock(InventoryMasterDataQueryApi.class);
        StockBalanceQueryPojo row = new StockBalanceQueryPojo();
        row.setId(1L); row.setSkuId(10L); row.setWarehouseId(20L); row.setQty(BigDecimal.TEN); row.setAvailableQty(BigDecimal.TEN);
        when(repository.queryList(any(), any(Integer.class), any(Integer.class))).thenReturn(List.of(row));
        when(repository.queryCount(any())).thenReturn(1L);
        when(masterData.findProductsByIds(any(), any())).thenReturn(Map.of(10L, new InventoryMasterDataQueryApi.ProductReference(10L, "SKU-10", "测试商品", "原料", "10kg", "袋", 1)));
        when(masterData.findWarehousesByIds(any(), any())).thenReturn(Map.of(20L, new InventoryMasterDataQueryApi.WarehouseReference(20L, "WH-20", "主仓")));

        StockQueryDTO dto = new StockQueryDTO(); dto.setCorpid("corp-001"); dto.setPageNum(1); dto.setPageSize(20); dto.setShowDisabled(1);
        var result = new StockQueryAppServiceImpl(repository, masterData).list(dto);

        assertEquals("测试商品", result.getList().getFirst().getSkuName());
        assertEquals("主仓", result.getList().getFirst().getWarehouseName());
    }
}
