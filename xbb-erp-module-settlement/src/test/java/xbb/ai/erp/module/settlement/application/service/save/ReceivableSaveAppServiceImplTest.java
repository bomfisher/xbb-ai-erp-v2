package xbb.ai.erp.module.settlement.application.service.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableMainDTO;
import xbb.ai.erp.module.settlement.admin.dto.ReceivableSaveDTO;
import xbb.ai.erp.module.settlement.application.port.ReceivableDraftRepository;
import xbb.ai.erp.module.settlement.application.validator.ReceivableSaveBusinessValidator;
import xbb.ai.erp.module.settlement.application.validator.ReceivableSaveCommonValidator;
import xbb.ai.erp.module.settlement.application.validator.ReceivableSaveProtocolValidator;
import xbb.ai.erp.module.settlement.domain.model.Receivable;
import xbb.ai.erp.module.settlement.domain.repository.ReceivableRepository;
import xbb.ai.erp.module.sales.contract.SalesInvoiceOpenAmountApi;

class ReceivableSaveAppServiceImplTest {

    @Test
    void shouldInitializeWriteOffAmountsAndStatusWhenCreating() {
        ReceivableRepository repository = mock(ReceivableRepository.class);
        doAnswer(invocation -> {
            Receivable receivable = invocation.getArgument(0);
            receivable.setId(9001L);
            return 9001L;
        }).when(repository).insert(any(Receivable.class));
        ReceivableSaveAppServiceImpl service = new ReceivableSaveAppServiceImpl(
            repository,
            mock(SalesInvoiceOpenAmountApi.class),
            mock(ReceivableDraftRepository.class),
            mock(ReceivableSaveProtocolValidator.class),
            mock(ReceivableSaveCommonValidator.class),
            mock(ReceivableSaveBusinessValidator.class)
        );
        ReceivableSaveDTO dto = new ReceivableSaveDTO();
        dto.setCorpid("corp-001");
        dto.setUserId("user-001");
        ReceivableMainDTO main = new ReceivableMainDTO();
        main.setCustomerId(101L);
        main.setSourceType("MANUAL_ADJUSTMENT");
        main.setReceivableDate(1_723_456_789_000L);
        main.setAmount(new BigDecimal("88.80"));
        dto.setMain(main);

        assertEquals(9001L, service.save(dto));
        org.mockito.ArgumentCaptor<Receivable> captor = org.mockito.ArgumentCaptor.forClass(Receivable.class);
        org.mockito.Mockito.verify(repository).insert(captor.capture());
        assertEquals(BigDecimal.ZERO, captor.getValue().getWrittenOffAmount());
        assertEquals(new BigDecimal("88.80"), captor.getValue().getRemainingAmount());
        assertEquals(0, captor.getValue().getStatus());
    }
}
