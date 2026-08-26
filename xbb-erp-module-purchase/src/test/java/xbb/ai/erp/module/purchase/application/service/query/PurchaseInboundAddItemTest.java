package xbb.ai.erp.module.purchase.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundItemDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSelectionFillDTO;
import xbb.ai.erp.module.purchase.application.field.PurchaseInboundFieldFactory;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

class PurchaseInboundAddItemTest {

    @Test
    void should_generate_purchase_inbound_number_for_new_item() {
        BizNoGenerator generator = Mockito.mock(BizNoGenerator.class);
        Mockito.when(generator.next("corp-a", "PURCHASE_INBOUND")).thenReturn("PI-20260819-00001");
        PurchaseInboundFieldFactory fieldFactory = Mockito.mock(PurchaseInboundFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        PurchaseInboundQueryAppServiceImpl service = new PurchaseInboundQueryAppServiceImpl(
            Mockito.mock(PurchaseInboundRepository.class), Mockito.mock(PurchaseInboundItemRepository.class),
            Mockito.mock(PurchaseOrderRepository.class), Mockito.mock(PurchaseOrderItemRepository.class),
            fieldFactory, null, null, generator);
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-a");

        assertEquals("PI-20260819-00001", service.addItem(dto).getData().getMain().getInboundNo());
        Mockito.verify(generator).next("corp-a", "PURCHASE_INBOUND");
    }

    @Test
    void should_return_public_linkage_config_for_new_item() {
        PurchaseInboundFieldFactory fieldFactory = Mockito.mock(PurchaseInboundFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        PurchaseInboundQueryAppServiceImpl service = new PurchaseInboundQueryAppServiceImpl(
            Mockito.mock(PurchaseInboundRepository.class), Mockito.mock(PurchaseInboundItemRepository.class),
            Mockito.mock(PurchaseOrderRepository.class), Mockito.mock(PurchaseOrderItemRepository.class),
            fieldFactory, null, null, Mockito.mock(BizNoGenerator.class));
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-a");

        Map<String, Object> linkageConfig = service.addItem(dto).getLinkageConfig();

        assertNotNull(linkageConfig);
        assertNotNull(linkageConfig.get("clearRules"));
        assertEquals("main.warehouseId", ((Map<?, ?>) linkageConfig.get("warehouseSync")).get("headerAttr"));
    }

    @Test
    void should_fill_item_warehouse_from_purchase_order() {
        PurchaseOrderRepository purchaseOrderRepository = Mockito.mock(PurchaseOrderRepository.class);
        PurchaseOrderItemRepository purchaseOrderItemRepository = Mockito.mock(PurchaseOrderItemRepository.class);
        PurchaseOrder order = new PurchaseOrder();
        order.setId(1001L);
        order.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        PurchaseOrderItem orderItem = new PurchaseOrderItem();
        orderItem.setId(2001L);
        orderItem.setSkuId(3001L);
        orderItem.setSkuName("产品 A");
        orderItem.setUnitName("件");
        orderItem.setWarehouseId(4001L);
        orderItem.setQty(BigDecimal.TEN);
        orderItem.setInboundQty(BigDecimal.ZERO);
        orderItem.setUnitPrice(BigDecimal.ONE);
        Mockito.when(purchaseOrderRepository.findById("corp-a", 1001L)).thenReturn(order);
        Mockito.when(purchaseOrderItemRepository.findByCondition(
            Map.of("corpid", "corp-a", "purchaseOrderId", 1001L))).thenReturn(List.of(orderItem));
        PurchaseInboundQueryAppServiceImpl service = new PurchaseInboundQueryAppServiceImpl(
            Mockito.mock(PurchaseInboundRepository.class), Mockito.mock(PurchaseInboundItemRepository.class),
            purchaseOrderRepository, purchaseOrderItemRepository,
            Mockito.mock(PurchaseInboundFieldFactory.class), null, null, Mockito.mock(BizNoGenerator.class));
        PurchaseInboundSelectionFillDTO dto = new PurchaseInboundSelectionFillDTO();
        dto.setCorpid("corp-a");
        dto.setFieldAttr("main.purchaseOrderId");
        dto.setReferenceId(1001L);

        Map<String, Object> patch = service.selectionFill(dto).getPatch();
        List<?> items = (List<?>) patch.get("items");
        PurchaseInboundItemDTO item = (PurchaseInboundItemDTO) items.getFirst();

        assertEquals(4001L, item.getWarehouseId());
        assertEquals(new BigDecimal("10.00"), patch.get("main.totalAmount"));
    }
}
