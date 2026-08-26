package xbb.ai.erp.module.inventory.application.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.inventory.admin.dto.StockTransactionQueryDTO;
import xbb.ai.erp.module.inventory.application.service.impl.StockTransactionQueryAppServiceImpl;
import xbb.ai.erp.module.inventory.domain.pojo.StockTransactionQueryPojo;
import xbb.ai.erp.module.inventory.domain.repository.StockTransactionRepository;
import xbb.ai.erp.module.masterdata.contract.InventoryMasterDataQueryApi;
import xbb.ai.erp.module.purchase.contract.InventoryPurchaseQueryApi;
import xbb.ai.erp.module.sales.contract.InventorySalesQueryApi;

class StockTransactionQueryAppServiceImplTest {
    @Test
    void shouldRenderSalesOutboundDocumentNumber() {
        StockTransactionRepository repository = mock(StockTransactionRepository.class);
        InventoryMasterDataQueryApi masterData = mock(InventoryMasterDataQueryApi.class);
        InventoryPurchaseQueryApi purchase = mock(InventoryPurchaseQueryApi.class);
        InventorySalesQueryApi sales = mock(InventorySalesQueryApi.class);
        StockTransactionQueryPojo row = new StockTransactionQueryPojo();
        row.setId(1L); row.setSkuId(10L); row.setWarehouseId(20L); row.setSourceType("SALES_OUTBOUND"); row.setSourceId(30L); row.setQtyChange(new BigDecimal("-2"));
        when(repository.queryList(any(), any(Integer.class), any(Integer.class))).thenReturn(List.of(row));
        when(repository.queryCount(any())).thenReturn(1L);
        when(masterData.findProductsByIds(any(), any())).thenReturn(Map.of(10L, new InventoryMasterDataQueryApi.ProductReference(10L, "SKU-10", "测试商品", "成品", "标准", "件", 1)));
        when(masterData.findWarehousesByIds(any(), any())).thenReturn(Map.of(20L, new InventoryMasterDataQueryApi.WarehouseReference(20L, "WH-20", "主仓")));
        when(purchase.findInboundByIds(any(), any())).thenReturn(Map.of());
        when(sales.findOutboundByIds(any(), any())).thenReturn(Map.of(30L, new InventorySalesQueryApi.SourceDocumentReference(30L, "SO-00030", null, 1)));

        StockTransactionQueryDTO dto = new StockTransactionQueryDTO(); dto.setCorpid("corp-001"); dto.setPageNum(1); dto.setPageSize(20);
        var result = new StockTransactionQueryAppServiceImpl(repository, masterData, purchase, sales).list(dto);

        assertEquals("SO-00030", result.getList().getFirst().getSourceDocumentNo());
        assertEquals(new BigDecimal("2"), result.getList().getFirst().getOutboundQty());
    }
}
