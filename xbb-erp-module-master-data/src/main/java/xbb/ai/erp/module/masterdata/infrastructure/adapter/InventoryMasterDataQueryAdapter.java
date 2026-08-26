package xbb.ai.erp.module.masterdata.infrastructure.adapter;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.module.masterdata.contract.InventoryMasterDataQueryApi;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper.ProductSkuMapper;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper.ProductSpuMapper;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.mapper.WarehouseMapper;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.ProductSkuPO;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.ProductSpuPO;
import xbb.ai.erp.module.masterdata.infrastructure.persistence.po.WarehousePO;

@Service
@RequiredArgsConstructor
public class InventoryMasterDataQueryAdapter implements InventoryMasterDataQueryApi {
    private final ProductSkuMapper skuMapper;
    private final ProductSpuMapper spuMapper;
    private final WarehouseMapper warehouseMapper;

    @Override
    public Map<Long, ProductReference> findProductsByIds(String corpid, Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyMap();
        var skus = skuMapper.findByIds(corpid, ids);
        var spuIds = skus.stream().map(ProductSkuPO::getSpuId).toList();
        var spus = spuMapper.findByIds(corpid, spuIds)
            .stream().collect(Collectors.toMap(ProductSpuPO::getId, Function.identity()));
        return skus.stream().collect(Collectors.toMap(ProductSkuPO::getId, sku -> {
            var spu = spus.get(sku.getSpuId());
            return new ProductReference(sku.getId(), sku.getSkuCode(), sku.getSkuName(),
                spu == null ? null : spu.getCategoryName(), sku.getSpecification(), sku.getUnitName(),
                spu == null || sku.getEnabled() == null ? 1 : (spu.getEnabled() == null ? sku.getEnabled() : (spu.getEnabled() == 1 && sku.getEnabled() == 1 ? 1 : 0)));
        }));
    }

    @Override
    public Map<Long, WarehouseReference> findWarehousesByIds(String corpid, Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) return Collections.emptyMap();
        return warehouseMapper.findByIds(corpid, ids)
            .stream().collect(Collectors.toMap(WarehousePO::getId,
                item -> new WarehouseReference(item.getId(), item.getWarehouseCode(), item.getWarehouseName())));
    }
}
