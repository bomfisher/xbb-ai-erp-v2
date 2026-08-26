package xbb.ai.erp.module.settlement.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.ListBaseDTO;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.settlement.domain.repository.PaymentRepository;

class PaymentQueryAppServiceImplTest {

    @Test
    void shouldFilterSupplierPaymentsForPaymentList() {
        assertPaymentType(PaymentQueryAppServiceImpl::list, "SUPPLIER_PAYMENT");
    }

    @Test
    void shouldFilterAdvancePaymentsForAdvancePaymentList() {
        assertPaymentType(PaymentQueryAppServiceImpl::listAdvancePayment, "ADVANCE_PAYMENT");
    }

    private void assertPaymentType(PaymentListOperation operation, String expectedPaymentType) {
        PaymentRepository repository = mock(PaymentRepository.class);
        when(repository.findByCondition(any())).thenReturn(List.of());
        when(repository.count(any())).thenReturn(0L);
        ListValueRenderer renderer = mock(ListValueRenderer.class);
        when(renderer.render(any(), any(), any())).thenAnswer(invocation -> invocation.getArgument(2));
        PaymentQueryAppServiceImpl service = new PaymentQueryAppServiceImpl(repository, renderer,
            mock(BizNoGenerator.class));
        ListBaseDTO dto = new ListBaseDTO();
        dto.setCorpid("corp-001");

        operation.list(service, dto);

        ArgumentCaptor<Map<String, Object>> captor = ArgumentCaptor.forClass(Map.class);
        org.mockito.Mockito.verify(repository).findByCondition(captor.capture());
        assertEquals(expectedPaymentType, captor.getValue().get("paymentType"));
    }

    @FunctionalInterface
    private interface PaymentListOperation {
        void list(PaymentQueryAppServiceImpl service, ListBaseDTO dto);
    }
}
