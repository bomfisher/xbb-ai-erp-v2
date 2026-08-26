package xbb.ai.erp.module.purchase.application.service.save;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.admin.PurchaseInvoiceStatusEnum;
import xbb.ai.erp.module.purchase.admin.PurchaseInvoiceTypeEnum;
import xbb.ai.erp.module.purchase.application.port.PurchaseInvoiceDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInvoiceSaveBusinessValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInvoiceSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInvoiceSaveProtocolValidator;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoice;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLine;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInvoiceLineSource;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceLineRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceLineSourceRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInvoiceRepository;

class PurchaseInvoiceSaveAppServiceImplTest {

    @Test
    void postShouldRequireApprovalAndMarkInvoicePosted() {
        Fixture fixture = new Fixture();
        fixture.invoice.setAuditStatus(AuditStatusEnum.APPROVED.getCode());

        fixture.service.post(fixture.idDto());

        assertEquals(PurchaseInvoiceStatusEnum.POSTED.getCode(), fixture.invoice.getStatus());
        verify(fixture.purchaseInvoiceRepository).update(fixture.invoice);
    }

    @Test
    void voidInvoiceShouldRejectApprovedInvoice() {
        Fixture fixture = new Fixture();
        fixture.invoice.setAuditStatus(AuditStatusEnum.APPROVED.getCode());

        assertThrows(BizException.class, () -> fixture.service.voidInvoice(fixture.idDto()));
    }

    @Test
    void redFlushShouldCreateNegativeCreditInvoiceWithLinesAndSources() {
        Fixture fixture = new Fixture();
        fixture.invoice.setStatus(PurchaseInvoiceStatusEnum.POSTED.getCode());
        fixture.invoice.setUntaxedAmount(new BigDecimal("100"));
        fixture.invoice.setTaxAmount(new BigDecimal("13"));
        fixture.invoice.setAmount(new BigDecimal("113"));
        PurchaseInvoiceLine originalLine = line(10L, new BigDecimal("2"), new BigDecimal("113"));
        PurchaseInvoiceLineSource originalSource = source(10L, new BigDecimal("2"), new BigDecimal("113"));
        when(fixture.lineRepository.findByInvoiceId("corp-1", 1L)).thenReturn(List.of(originalLine));
        when(fixture.sourceRepository.findByInvoiceLineIds("corp-1", List.of(10L))).thenReturn(List.of(originalSource));
        when(fixture.purchaseInvoiceRepository.findByCondition(any())).thenReturn(List.of());
        when(fixture.purchaseInvoiceRepository.insert(any(PurchaseInvoice.class))).thenAnswer(invocation -> {
            PurchaseInvoice creditInvoice = invocation.getArgument(0);
            creditInvoice.setId(2L);
            return 2L;
        });
        doAnswer(invocation -> {
            List<PurchaseInvoiceLine> lines = invocation.getArgument(0);
            lines.get(0).setId(20L);
            return null;
        }).when(fixture.lineRepository).insertBatch(any());

        fixture.service.redFlush(fixture.idDto());

        ArgumentCaptor<PurchaseInvoice> invoiceCaptor = ArgumentCaptor.forClass(PurchaseInvoice.class);
        verify(fixture.purchaseInvoiceRepository).insert(invoiceCaptor.capture());
        PurchaseInvoice creditInvoice = invoiceCaptor.getValue();
        assertEquals(PurchaseInvoiceTypeEnum.CREDIT_NOTE.getCode(), creditInvoice.getInvoiceType());
        assertEquals(new BigDecimal("-113"), creditInvoice.getAmount());
        assertEquals(PurchaseInvoiceStatusEnum.POSTED.getCode(), creditInvoice.getStatus());

        ArgumentCaptor<List<PurchaseInvoiceLine>> lineCaptor = ArgumentCaptor.forClass(List.class);
        verify(fixture.lineRepository).insertBatch(lineCaptor.capture());
        assertEquals(new BigDecimal("-2"), lineCaptor.getValue().get(0).getQuantity());

        ArgumentCaptor<List<PurchaseInvoiceLineSource>> sourceCaptor = ArgumentCaptor.forClass(List.class);
        verify(fixture.sourceRepository).insertBatch(sourceCaptor.capture());
        assertEquals(20L, sourceCaptor.getValue().get(0).getPurchaseInvoiceLineId());
        assertEquals(new BigDecimal("-113"), sourceCaptor.getValue().get(0).getAmount());
    }

    private static PurchaseInvoiceLine line(Long id, BigDecimal quantity, BigDecimal amount) {
        PurchaseInvoiceLine line = new PurchaseInvoiceLine();
        line.setId(id);
        line.setLineNo(1);
        line.setQuantity(quantity);
        line.setAmount(amount);
        line.setUntaxedAmount(new BigDecimal("100"));
        line.setTaxAmount(new BigDecimal("13"));
        return line;
    }

    private static PurchaseInvoiceLineSource source(Long lineId, BigDecimal quantity, BigDecimal amount) {
        PurchaseInvoiceLineSource source = new PurchaseInvoiceLineSource();
        source.setPurchaseInvoiceLineId(lineId);
        source.setQuantity(quantity);
        source.setAmount(amount);
        source.setUntaxedAmount(new BigDecimal("100"));
        source.setTaxAmount(new BigDecimal("13"));
        return source;
    }

    private static class Fixture {
        private final PurchaseInvoiceRepository purchaseInvoiceRepository = mock(PurchaseInvoiceRepository.class);
        private final PurchaseInvoiceLineRepository lineRepository = mock(PurchaseInvoiceLineRepository.class);
        private final PurchaseInvoiceLineSourceRepository sourceRepository = mock(PurchaseInvoiceLineSourceRepository.class);
        private final PurchaseInvoice invoice = new PurchaseInvoice();
        private final PurchaseInvoiceSaveAppServiceImpl service;

        private Fixture() {
            invoice.setId(1L);
            invoice.setCorpid("corp-1");
            invoice.setInvoiceNo("PI-001");
            invoice.setInvoiceType(PurchaseInvoiceTypeEnum.PURCHASE.getCode());
            invoice.setStatus(PurchaseInvoiceStatusEnum.DRAFT.getCode());
            invoice.setAuditStatus(AuditStatusEnum.PENDING.getCode());
            when(purchaseInvoiceRepository.findById("corp-1", 1L)).thenReturn(invoice);
            BizNoGenerator bizNoGenerator = mock(BizNoGenerator.class);
            when(bizNoGenerator.next(eq("corp-1"), any())).thenReturn("PI-002");
            service = new PurchaseInvoiceSaveAppServiceImpl(
                purchaseInvoiceRepository,
                lineRepository,
                sourceRepository,
                bizNoGenerator,
                mock(PurchaseInvoiceDraftRepository.class),
                mock(PurchaseInvoiceSaveProtocolValidator.class),
                mock(PurchaseInvoiceSaveCommonValidator.class),
                mock(PurchaseInvoiceSaveBusinessValidator.class)
            );
        }

        private IdBaseDTO idDto() {
            IdBaseDTO dto = new IdBaseDTO();
            dto.setCorpid("corp-1");
            dto.setUserId("user-1");
            dto.setId(1L);
            return dto;
        }
    }
}
