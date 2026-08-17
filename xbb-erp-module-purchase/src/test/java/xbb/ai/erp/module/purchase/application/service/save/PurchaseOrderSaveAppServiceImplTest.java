package xbb.ai.erp.module.purchase.application.service.save;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.enums.InboundStatusEnum;
import xbb.ai.erp.base.common.enums.PaymentStatusEnum;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.application.port.PurchaseOrderDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveBusinessValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveProtocolValidator;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

class PurchaseOrderSaveAppServiceImplTest {

    @Test
    void should_initialize_document_statuses_for_new_purchase_order() {
        PurchaseOrderRepository purchaseOrderRepository = Mockito.mock(PurchaseOrderRepository.class);
        Mockito.when(purchaseOrderRepository.insert(Mockito.any(PurchaseOrder.class))).thenReturn(1L);
        PurchaseOrderSaveAppServiceImpl service = new PurchaseOrderSaveAppServiceImpl(
            purchaseOrderRepository,
            Mockito.mock(PurchaseOrderItemRepository.class),
            Mockito.mock(PurchaseOrderDraftRepository.class),
            Mockito.mock(PurchaseOrderSaveProtocolValidator.class),
            Mockito.mock(PurchaseOrderSaveCommonValidator.class),
            Mockito.mock(PurchaseOrderSaveBusinessValidator.class));
        PurchaseOrderSaveDTO dto = new PurchaseOrderSaveDTO();
        dto.setCorpid("corp-a");
        dto.setUserId("user-a");
        dto.setMain(new PurchaseOrderMainDTO());

        service.save(dto);

        ArgumentCaptor<PurchaseOrder> captor = ArgumentCaptor.forClass(PurchaseOrder.class);
        Mockito.verify(purchaseOrderRepository).insert(captor.capture());
        PurchaseOrder saved = captor.getValue();
        assertEquals(AuditStatusEnum.PENDING.getCode(), saved.getAuditStatus());
        assertEquals(InboundStatusEnum.NOT_INBOUNDED.getCode(), saved.getInboundStatus());
        assertEquals(PaymentStatusEnum.NOT_PAID.getCode(), saved.getPaymentStatus());
    }
}
