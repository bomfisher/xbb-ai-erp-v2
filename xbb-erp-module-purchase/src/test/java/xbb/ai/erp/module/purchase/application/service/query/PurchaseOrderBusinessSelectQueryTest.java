package xbb.ai.erp.module.purchase.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.List;
import java.util.Map;
import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderBusinessSelectQueryDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderBusinessSelectOptionVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;

class PurchaseOrderBusinessSelectQueryTest {
    private final PurchaseOrderQueryAppServiceImpl queryService = new PurchaseOrderQueryAppServiceImpl(
        new StubPurchaseOrderRepository(), new StubPurchaseOrderItemRepository(), null, null, null, null);

    @Test
    void shouldLimitBusinessSelectResultsToCurrentTenantAndKeyword() {
        PurchaseOrderBusinessSelectQueryDTO dto = query("corp-a");
        dto.setKeyword("PO-002");

        List<PurchaseOrderBusinessSelectOptionVO> options = queryService.businessSelectQuickSearch(dto);

        assertEquals(1, options.size());
        assertEquals(2L, options.getFirst().getId());
        assertEquals("PO-002 杭州供应商", options.getFirst().getLabel());
    }

    @Test
    void shouldPageBusinessSelectResultsAndHideOtherTenantsById() {
        PurchaseOrderBusinessSelectQueryDTO dialogDto = query("corp-a");
        dialogDto.setPageNum(2);
        dialogDto.setPageSize(1);

        ListBaseVO<PurchaseOrderBusinessSelectOptionVO> page = queryService.businessSelectDialogSearch(dialogDto);
        assertEquals(1, page.getList().size());
        assertEquals(2L, page.getList().getFirst().getId());

        PurchaseOrderBusinessSelectQueryDTO idDto = query("corp-a");
        idDto.setId(3L);
        assertNull(queryService.businessSelectGetById(idDto));
    }

    @Test
    void shouldFilterBusinessSelectOrdersBySupplierWhenProvided() {
        PurchaseOrderBusinessSelectQueryDTO dto = query("corp-a");
        dto.setSupplierId(2L);

        List<PurchaseOrderBusinessSelectOptionVO> options = queryService.businessSelectQuickSearch(dto);

        assertEquals(1, options.size());
        assertEquals(2L, options.getFirst().getId());
    }

    private PurchaseOrderBusinessSelectQueryDTO query(String corpid) {
        PurchaseOrderBusinessSelectQueryDTO dto = new PurchaseOrderBusinessSelectQueryDTO();
        dto.setCorpid(corpid);
        return dto;
    }

    private static class StubPurchaseOrderRepository implements PurchaseOrderRepository {
        private final List<PurchaseOrder> orders = List.of(
            order(1L, "corp-a", "PO-001", "宁波供应商", AuditStatusEnum.NO_NEED_APPROVED.getCode()),
            order(2L, "corp-a", "PO-002", "杭州供应商", AuditStatusEnum.APPROVED.getCode()),
            order(3L, "corp-b", "PO-003", "上海供应商", AuditStatusEnum.PENDING.getCode()));

        @Override
        public PurchaseOrder findById(String corpid, Long id) {
            return orders.stream().filter(order -> order.getCorpid().equals(corpid) && order.getId().equals(id)).findFirst().orElse(null);
        }

        @Override
        public List<PurchaseOrder> findByIds(String corpid, java.util.Collection<Long> ids) {
            return orders.stream()
                .filter(order -> order.getCorpid().equals(corpid) && ids.contains(order.getId()))
                .toList();
        }

        @Override
        public List<PurchaseOrder> findByCondition(Map<String, Object> conditionMap) {
            return orders.stream().filter(order -> order.getCorpid().equals(conditionMap.get("corpid"))).toList();
        }

        @Override public Long insert(PurchaseOrder purchaseOrder) { throw new UnsupportedOperationException(); }
        @Override public void insertBatch(List<PurchaseOrder> purchaseOrderList) { throw new UnsupportedOperationException(); }
        @Override public void removeById(String corpid, Long id) { throw new UnsupportedOperationException(); }
        @Override public void removeBatchByIds(String corpid, List<Long> ids) { throw new UnsupportedOperationException(); }
        @Override public void update(PurchaseOrder purchaseOrder) { throw new UnsupportedOperationException(); }
        @Override public Long count(Map<String, Object> conditionMap) { return 0L; }

        private static PurchaseOrder order(Long id, String corpid, String orderNo, String supplierName, Integer auditStatus) {
            PurchaseOrder order = new PurchaseOrder();
            order.setId(id);
            order.setCorpid(corpid);
            order.setOrderNo(orderNo);
            order.setSupplierName(supplierName);
            order.setSupplierId(id);
            order.setAuditStatus(auditStatus);
            return order;
        }
    }

    private static class StubPurchaseOrderItemRepository implements PurchaseOrderItemRepository {
        @Override
        public List<PurchaseOrderItem> findByCondition(Map<String, Object> conditionMap) {
            Long orderId = (Long) conditionMap.get("purchaseOrderId");
            PurchaseOrderItem item = new PurchaseOrderItem();
            item.setId(orderId * 10);
            item.setPurchaseOrderId(orderId);
            item.setQty(BigDecimal.TEN);
            item.setInboundQty(orderId.equals(2L) ? BigDecimal.ONE : BigDecimal.ZERO);
            return List.of(item);
        }

        @Override public Long insert(PurchaseOrderItem purchaseOrderItem) { throw new UnsupportedOperationException(); }
        @Override public void insertBatch(List<PurchaseOrderItem> purchaseOrderItemList) { throw new UnsupportedOperationException(); }
        @Override public void removeById(String corpid, Long id) { throw new UnsupportedOperationException(); }
        @Override public void removeBatchByIds(String corpid, List<Long> ids) { throw new UnsupportedOperationException(); }
        @Override public void update(PurchaseOrderItem purchaseOrderItem) { throw new UnsupportedOperationException(); }
        @Override public PurchaseOrderItem findById(String corpid, Long id) { return null; }
        @Override public Long count(Map<String, Object> conditionMap) { return 0L; }
    }
}
