package xbb.ai.erp.module.purchase.application.service.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.enums.InboundStatusEnum;
import xbb.ai.erp.base.common.enums.PurchaseInboundStatusEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.inventory.contract.InventoryCommandApi;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundConfirmDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundItemDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSubmitSaveDTO;
import xbb.ai.erp.module.purchase.application.port.PurchaseInboundApprovalPolicy;
import xbb.ai.erp.module.purchase.application.port.PurchaseInboundDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundSaveBusinessValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundSaveProtocolValidator;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

class PurchaseInboundSaveAppServiceImplTest {

    @Test
    void should_initialize_audit_status_as_pending_for_new_inbound() {
        PurchaseInboundRepository inboundRepository = Mockito.mock(PurchaseInboundRepository.class);
        Mockito.when(inboundRepository.insert(Mockito.any(PurchaseInbound.class))).thenReturn(1L);
        PurchaseInboundSaveAppServiceImpl service = service(inboundRepository, false);
        PurchaseInboundSaveDTO dto = new PurchaseInboundSaveDTO();
        dto.setCorpid("corp-a");
        dto.setUserId("user-a");
        dto.setMain(new PurchaseInboundMainDTO());
        dto.getMain().setStatus(PurchaseInboundStatusEnum.INVENTORY_POSTED.getCode());

        service.save(dto);

        ArgumentCaptor<PurchaseInbound> captor = ArgumentCaptor.forClass(PurchaseInbound.class);
        Mockito.verify(inboundRepository).insert(captor.capture());
        PurchaseInbound saved = captor.getValue();
        assertEquals(AuditStatusEnum.PENDING.getCode(), saved.getAuditStatus());
        assertEquals(PurchaseInboundStatusEnum.SUBMITTED.getCode(), saved.getStatus());
    }

    @Test
    void should_reject_confirmation_before_audit_approval() {
        PurchaseInboundRepository inboundRepository = Mockito.mock(PurchaseInboundRepository.class);
        PurchaseInbound inbound = new PurchaseInbound();
        inbound.setStatus(PurchaseInboundStatusEnum.SUBMITTED.getCode());
        inbound.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        Mockito.when(inboundRepository.findById("corp-a", 1L)).thenReturn(inbound);
        PurchaseInboundSaveAppServiceImpl service = service(inboundRepository, true);
        PurchaseInboundConfirmDTO dto = new PurchaseInboundConfirmDTO();
        dto.setCorpid("corp-a");
        dto.setUserId("user-a");
        dto.setId(1L);

        assertThrows(BizException.class, () -> service.confirmInbound(dto));
    }

    @Test
    void should_update_purchase_order_inbound_progress_when_pending_inbound_is_submitted() {
        PurchaseInboundRepository inboundRepository = Mockito.mock(PurchaseInboundRepository.class);
        PurchaseInboundItemRepository inboundItemRepository = Mockito.mock(PurchaseInboundItemRepository.class);
        PurchaseOrderRepository orderRepository = Mockito.mock(PurchaseOrderRepository.class);
        PurchaseOrderItemRepository orderItemRepository = Mockito.mock(PurchaseOrderItemRepository.class);
        Mockito.when(inboundRepository.insert(Mockito.any(PurchaseInbound.class))).thenReturn(1L);
        PurchaseOrder order = new PurchaseOrder();
        order.setId(2L);
        Mockito.when(orderRepository.findById("corp-a", 2L)).thenReturn(order);
        PurchaseOrderItem orderItem = new PurchaseOrderItem();
        orderItem.setId(4L);
        orderItem.setPurchaseOrderId(2L);
        orderItem.setQty(new BigDecimal("10"));
        orderItem.setInboundQty(new BigDecimal("2"));
        Mockito.when(orderItemRepository.findByCondition(Mockito.anyMap())).thenReturn(List.of(orderItem));
        PurchaseInboundSaveAppServiceImpl service = service(inboundRepository, inboundItemRepository, orderRepository,
            orderItemRepository, true);
        PurchaseInboundItemDTO inboundItem = new PurchaseInboundItemDTO();
        inboundItem.setPurchaseOrderItemId(4L);
        inboundItem.setWarehouseId(5L);
        inboundItem.setSkuId(6L);
        inboundItem.setQty(new BigDecimal("3"));
        inboundItem.setUnitPrice(new BigDecimal("10"));
        PurchaseInboundMainDTO main = new PurchaseInboundMainDTO();
        main.setPurchaseOrderId(2L);
        PurchaseInboundSubmitSaveDTO dto = new PurchaseInboundSubmitSaveDTO();
        dto.setCorpid("corp-a");
        dto.setUserId("user-a");
        dto.setMain(main);
        dto.setItems(List.of(inboundItem));

        service.saveAndSubmit(dto);

        assertEquals(new BigDecimal("5"), orderItem.getInboundQty());
        assertEquals(InboundStatusEnum.PARTIALLY_INBOUNDED.getCode(), orderItem.getInboundStatus());
        assertEquals(InboundStatusEnum.PARTIALLY_INBOUNDED.getCode(), order.getInboundStatus());
    }

    private static PurchaseInboundSaveAppServiceImpl service(PurchaseInboundRepository inboundRepository,
                                                              boolean requiresApproval) {
        return service(inboundRepository, Mockito.mock(PurchaseInboundItemRepository.class),
            Mockito.mock(PurchaseOrderRepository.class), Mockito.mock(PurchaseOrderItemRepository.class), requiresApproval);
    }

    private static PurchaseInboundSaveAppServiceImpl service(PurchaseInboundRepository inboundRepository,
                                                              PurchaseInboundItemRepository inboundItemRepository,
                                                              PurchaseOrderRepository orderRepository,
                                                              PurchaseOrderItemRepository orderItemRepository,
                                                              boolean requiresApproval) {
        PurchaseInboundApprovalPolicy approvalPolicy = corpid -> requiresApproval;
        return new PurchaseInboundSaveAppServiceImpl(
            inboundRepository,
            inboundItemRepository,
            orderRepository,
            orderItemRepository,
            Mockito.mock(PurchaseInboundDraftRepository.class),
            Mockito.mock(PurchaseInboundSaveProtocolValidator.class),
            Mockito.mock(PurchaseInboundSaveCommonValidator.class),
            Mockito.mock(PurchaseInboundSaveBusinessValidator.class),
            approvalPolicy,
            Mockito.mock(InventoryCommandApi.class));
    }
}
