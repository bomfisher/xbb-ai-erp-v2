package xbb.ai.erp.module.inventory.application.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.inventory.admin.dto.StockQueryDTO;
import xbb.ai.erp.module.inventory.admin.vo.StockQueryItemVO;
import xbb.ai.erp.module.inventory.application.service.StockQueryAppService;
import xbb.ai.erp.module.inventory.domain.pojo.StockBalanceQueryPojo;
import xbb.ai.erp.module.inventory.domain.repository.StockBalanceRepository;
import xbb.ai.erp.module.masterdata.contract.InventoryMasterDataQueryApi;

@Service
@RequiredArgsConstructor
public class StockQueryAppServiceImpl implements StockQueryAppService {
    private final StockBalanceRepository repository;
    private final InventoryMasterDataQueryApi masterDataQueryApi;

    @Override
    public ListBaseVO<StockQueryItemVO> list(StockQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        int page = Math.max(Objects.requireNonNullElse(dto.getPageNum(), 1), 1);
        int size = Math.min(Math.max(Objects.requireNonNullElse(dto.getPageSize(), 20), 1), 200);
        boolean postFilter = (dto.getCategoryName() != null && !dto.getCategoryName().isBlank()) || (dto.getSpecification() != null && !dto.getSpecification().isBlank()) || Objects.equals(dto.getShowDisabled(), 0);
        List<StockBalanceQueryPojo> rows = repository.queryList(dto, postFilter ? 0 : (page - 1) * size, postFilter ? 10000 : size);
        Map<Long, InventoryMasterDataQueryApi.ProductReference> products = masterDataQueryApi.findProductsByIds(
            dto.getCorpid(), rows.stream().map(StockBalanceQueryPojo::getSkuId).collect(Collectors.toSet()));
        Map<Long, InventoryMasterDataQueryApi.WarehouseReference> warehouses = masterDataQueryApi.findWarehousesByIds(
            dto.getCorpid(), rows.stream().map(StockBalanceQueryPojo::getWarehouseId).collect(Collectors.toSet()));
        List<StockQueryItemVO> items = rows.stream().filter(row -> {
            var product = products.get(row.getSkuId());
            return product != null && (dto.getCategoryName() == null || dto.getCategoryName().isBlank() || dto.getCategoryName().equals(product.categoryName())) && (dto.getSpecification() == null || dto.getSpecification().isBlank() || product.specification() != null && product.specification().contains(dto.getSpecification())) && (Objects.equals(dto.getShowDisabled(), 1) || !Objects.equals(product.enabled(), 0));
        }).map(row -> {
            var product = products.get(row.getSkuId());
            var warehouse = warehouses.get(row.getWarehouseId());
            StockQueryItemVO item = new StockQueryItemVO();
            item.setId(row.getId()); item.setSkuId(row.getSkuId()); item.setWarehouseId(row.getWarehouseId());
            if (product != null) { item.setSkuCode(product.code()); item.setSkuName(product.name()); item.setCategoryName(product.categoryName()); item.setSpecification(product.specification()); }
            if (warehouse != null) { item.setWarehouseCode(warehouse.code()); item.setWarehouseName(warehouse.name()); }
            item.setQty(row.getQty()); item.setLockedQty(row.getLockedQty()); item.setAvailableQty(row.getAvailableQty());
            item.setTotalCost(row.getTotalCost()); item.setUnitCost(row.getUnitCost());
            return item;
        }).toList();
        ListBaseVO<StockQueryItemVO> result = new ListBaseVO<>();
        if (postFilter) { int from = Math.min((page - 1) * size, items.size()); items = items.subList(from, Math.min(from + size, items.size())); }
        result.setList(items);
        Long count = postFilter ? (long) rows.stream().filter(row -> { var p = products.get(row.getSkuId()); return p != null && (dto.getCategoryName() == null || dto.getCategoryName().isBlank() || dto.getCategoryName().equals(p.categoryName())) && (dto.getSpecification() == null || dto.getSpecification().isBlank() || p.specification() != null && p.specification().contains(dto.getSpecification())) && (Objects.equals(dto.getShowDisabled(), 1) || !Objects.equals(p.enabled(), 0)); }).count() : repository.queryCount(dto);
        result.setPageHelper(new ListBaseVO.PageHelper(page, count == null ? 0 : count.intValue()));
        return result;
    }
}
