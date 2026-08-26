package xbb.ai.erp.module.masterdata.contract;

import java.util.Collection;
import java.util.Map;

public interface InventoryMasterDataQueryApi {
    Map<Long, ProductReference> findProductsByIds(String corpid, Collection<Long> skuIds);
    Map<Long, WarehouseReference> findWarehousesByIds(String corpid, Collection<Long> warehouseIds);
    record ProductReference(Long id, String code, String name, String categoryName, String specification, String unitName, Integer enabled) {}
    record WarehouseReference(Long id, String code, String name) {}
}
