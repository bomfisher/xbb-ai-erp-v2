package xbb.ai.erp.module.inventory.application.service.impl;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.module.inventory.admin.dto.StockTransactionQueryDTO;
import xbb.ai.erp.module.inventory.admin.vo.StockTransactionQueryItemVO;
import xbb.ai.erp.module.inventory.application.service.StockTransactionQueryAppService;
import xbb.ai.erp.module.inventory.domain.pojo.StockTransactionQueryPojo;
import xbb.ai.erp.module.inventory.domain.repository.StockTransactionRepository;
import xbb.ai.erp.module.masterdata.contract.InventoryMasterDataQueryApi;
import xbb.ai.erp.module.purchase.contract.InventoryPurchaseQueryApi;
import xbb.ai.erp.module.sales.contract.InventorySalesQueryApi;

@Service
@RequiredArgsConstructor
public class StockTransactionQueryAppServiceImpl implements StockTransactionQueryAppService {
    private final StockTransactionRepository repository;
    private final InventoryMasterDataQueryApi masterDataQueryApi;
    private final InventoryPurchaseQueryApi purchaseQueryApi;
    private final InventorySalesQueryApi salesQueryApi;

    @Override
    public ListBaseVO<StockTransactionQueryItemVO> list(StockTransactionQueryDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        int page = Math.max(Objects.requireNonNullElse(dto.getPageNum(), 1), 1);
        int size = Math.min(Math.max(Objects.requireNonNullElse(dto.getPageSize(), 20), 1), 200);
        boolean postFilter = (dto.getCategoryName() != null && !dto.getCategoryName().isBlank()) || (dto.getSpecification() != null && !dto.getSpecification().isBlank());
        List<StockTransactionQueryPojo> rows = repository.queryList(dto, postFilter ? 0 : (page - 1) * size, postFilter ? 10000 : size);
        Map<Long, InventoryMasterDataQueryApi.ProductReference> products = masterDataQueryApi.findProductsByIds(dto.getCorpid(), rows.stream().map(StockTransactionQueryPojo::getSkuId).collect(Collectors.toSet()));
        Map<Long, InventoryMasterDataQueryApi.WarehouseReference> warehouses = masterDataQueryApi.findWarehousesByIds(dto.getCorpid(), rows.stream().map(StockTransactionQueryPojo::getWarehouseId).collect(Collectors.toSet()));
        Set<Long> inboundIds = rows.stream().filter(row -> "PURCHASE_INBOUND".equals(row.getSourceType())).map(StockTransactionQueryPojo::getSourceId).collect(Collectors.toSet());
        Set<Long> outboundIds = rows.stream().filter(row -> "SALES_OUTBOUND".equals(row.getSourceType())).map(StockTransactionQueryPojo::getSourceId).collect(Collectors.toSet());
        Map<Long, InventoryPurchaseQueryApi.SourceDocumentReference> inbounds = purchaseQueryApi.findInboundByIds(dto.getCorpid(), inboundIds);
        Map<Long, InventorySalesQueryApi.SourceDocumentReference> outbounds = salesQueryApi.findOutboundByIds(dto.getCorpid(), outboundIds);
        List<StockTransactionQueryItemVO> items = rows.stream().filter(row -> {
            var product = products.get(row.getSkuId());
            return product != null && (dto.getCategoryName() == null || dto.getCategoryName().isBlank() || dto.getCategoryName().equals(product.categoryName())) && (dto.getSpecification() == null || dto.getSpecification().isBlank() || product.specification() != null && product.specification().contains(dto.getSpecification()));
        }).map(row -> {
            var product = products.get(row.getSkuId());
            var warehouse = warehouses.get(row.getWarehouseId());
            StockTransactionQueryItemVO item = new StockTransactionQueryItemVO();
            item.setId(row.getId());
            item.setSourceType(row.getSourceType());
            item.setSourceId(row.getSourceId());
            item.setOccurredAt(row.getOccurredAt());
            if (product != null) {
                item.setSkuCode(product.code());
                item.setSkuName(product.name());
                item.setSpecification(product.specification());
                item.setUnitName(product.unitName());
            }
            if (warehouse != null) item.setWarehouseName(warehouse.name());
            if (BusinessCodeEnum.PURCHASE_INBOUND.getCode().equals(row.getSourceType()))
                item.setSourceDocumentNo(inbounds.get(row.getSourceId()) == null ? null : inbounds.get(row.getSourceId()).documentNo());
            if (BusinessCodeEnum.SALES_OUTBOUND.getCode().equals(row.getSourceType()))
                item.setSourceDocumentNo(outbounds.get(row.getSourceId()) == null ? null : outbounds.get(row.getSourceId()).documentNo());
            if (row.getQtyChange() != null && row.getQtyChange().signum() > 0) {
                item.setInboundQty(row.getQtyChange());
                item.setInboundUnitCost(row.getInboundUnitCost());
                item.setInboundCost(row.getInboundCost());
            }
            if (row.getQtyChange() != null && row.getQtyChange().signum() < 0) {
                item.setOutboundQty(row.getQtyChange().negate());
                item.setOutboundUnitCost(row.getOutboundUnitCost());
                item.setOutboundCost(row.getOutboundCost());
            }
            item.setQtyAfter(row.getQtyAfter());
            item.setUnitCostAfter(row.getUnitCostAfter());
            item.setTotalCostAfter(row.getTotalCostAfter());
            return item;
        }).toList();
        if (postFilter) {
            int from = Math.min((page - 1) * size, items.size());
            items = items.subList(from, Math.min(from + size, items.size()));
        }
        ListBaseVO<StockTransactionQueryItemVO> result = new ListBaseVO<>();
        result.setList(items);
        Long count = postFilter ? (long) rows.stream().filter(row -> {
            var p = products.get(row.getSkuId());
            return p != null && (dto.getCategoryName() == null || dto.getCategoryName().isBlank() || dto.getCategoryName().equals(p.categoryName())) && (dto.getSpecification() == null || dto.getSpecification().isBlank() || p.specification() != null && p.specification().contains(dto.getSpecification()));
        }).count() : repository.queryCount(dto);
        result.setPageHelper(new ListBaseVO.PageHelper(page, count == null ? 0 : count.intValue()));
        return result;
    }
}
