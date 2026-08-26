package xbb.ai.erp.module.inventory.contract;

import java.math.BigDecimal;

public interface InventoryStockQueryApi {
    BigDecimal queryInstantQty(String corpid, Long skuId, Long warehouseId);
}
