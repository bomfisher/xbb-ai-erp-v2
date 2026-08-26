package xbb.ai.erp.module.sales.application.service.save;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.module.sales.application.port.SalesInvoiceDraftRepository;
import xbb.ai.erp.module.sales.application.validator.SalesInvoiceSaveBusinessValidator;
import xbb.ai.erp.module.sales.application.validator.SalesInvoiceSaveCommonValidator;
import xbb.ai.erp.module.sales.application.validator.SalesInvoiceSaveProtocolValidator;
import xbb.ai.erp.module.sales.domain.model.SalesInvoice;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceLineRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceLineSourceRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceRepository;
import xbb.ai.erp.module.settlement.contract.InvoiceReceivableApi;
import xbb.ai.erp.module.system.contract.BooleanConfigKeyEnum;
import xbb.ai.erp.module.system.contract.BusinessConfigQueryApi;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.junit.jupiter.api.Assertions.assertEquals;

class SalesInvoiceSaveAppServiceImplTest {

    @Test
    void shouldMarkRequiredArgumentsConstructorForSpringInjection() {
        long autowiredConstructorCount = java.util.Arrays.stream(SalesInvoiceSaveAppServiceImpl.class.getConstructors())
            .filter(constructor -> constructor.isAnnotationPresent(Autowired.class))
            .count();

        assertEquals(1, autowiredConstructorCount);
    }

    @Test
    void should_not_create_receivable_after_post_when_auto_create_is_disabled() {
        Fixture fixture = new Fixture(false);

        fixture.service.post(fixture.postDto());

        verify(fixture.invoiceReceivableApi, never()).createForInvoice(any());
        verify(fixture.salesInvoiceRepository).update(fixture.invoice);
    }

    @Test
    void should_create_receivable_after_post_when_auto_create_is_enabled() {
        Fixture fixture = new Fixture(true);

        fixture.service.post(fixture.postDto());

        verify(fixture.invoiceReceivableApi).createForInvoice(any());
        verify(fixture.salesInvoiceRepository).update(fixture.invoice);
    }

    private static class Fixture {
        private final SalesInvoiceRepository salesInvoiceRepository = mock(SalesInvoiceRepository.class);
        private final InvoiceReceivableApi invoiceReceivableApi = mock(InvoiceReceivableApi.class);
        private final BusinessConfigQueryApi businessConfigQueryApi = mock(BusinessConfigQueryApi.class);
        private final SalesInvoice invoice = new SalesInvoice();
        private final SalesInvoiceSaveAppServiceImpl service;

        private Fixture(boolean autoCreateReceivable) {
            invoice.setId(1L);
            invoice.setCorpid("corp-1");
            invoice.setCustomerId(2L);
            invoice.setInvoiceDate(1_723_456_789_000L);
            invoice.setDueDate(1_723_456_789_000L);
            invoice.setAmount(BigDecimal.TEN);
            invoice.setStatus("DRAFT");
            invoice.setAuditStatus(AuditStatusEnum.APPROVED.getCode());

            when(salesInvoiceRepository.findById("corp-1", 1L)).thenReturn(invoice);
            when(businessConfigQueryApi.get(eq("corp-1"),
                eq(BooleanConfigKeyEnum.SALES_INVOICE_AUTO_CREATE_RECEIVABLE))).thenReturn(autoCreateReceivable);

            service = new SalesInvoiceSaveAppServiceImpl(
                salesInvoiceRepository,
                mock(SalesInvoiceLineRepository.class),
                mock(SalesInvoiceLineSourceRepository.class),
                mock(BizNoGenerator.class),
                invoiceReceivableApi,
                businessConfigQueryApi,
                mock(SalesInvoiceDraftRepository.class),
                mock(SalesInvoiceSaveProtocolValidator.class),
                mock(SalesInvoiceSaveCommonValidator.class),
                mock(SalesInvoiceSaveBusinessValidator.class)
            );
        }

        private IdBaseDTO postDto() {
            IdBaseDTO dto = new IdBaseDTO();
            dto.setCorpid("corp-1");
            dto.setUserId("user-1");
            dto.setId(1L);
            return dto;
        }
    }
}
