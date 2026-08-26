package xbb.ai.erp.module.settlement.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.common.application.render.ListValueRenderer;
import xbb.ai.erp.module.purchase.contract.PurchaseInvoiceOpenAmountApi;
import xbb.ai.erp.module.settlement.admin.dto.SettlementSelectionFillDTO;
import xbb.ai.erp.module.settlement.domain.repository.PayableRepository;

class PayableQueryAppServiceImplTest {

    @Test
    void shouldGeneratePayableNumberWhenLoadingNewPayableForm() {
        BizNoGenerator bizNoGenerator = mock(BizNoGenerator.class);
        when(bizNoGenerator.next("corp-001", "PAYABLE")).thenReturn("YF-202608250001");
        PayableQueryAppServiceImpl service = new PayableQueryAppServiceImpl(
            mock(PayableRepository.class),
            mock(ListValueRenderer.class),
            mock(PurchaseInvoiceOpenAmountApi.class),
            bizNoGenerator
        );
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-001");

        assertEquals("YF-202608250001", service.addItem(dto).getData().getMain().getPayableNo());
    }

    @Test
    void shouldFillPayableAmountFromSelectedPurchaseInvoice() {
        PurchaseInvoiceOpenAmountApi invoiceOpenAmountApi = mock(PurchaseInvoiceOpenAmountApi.class);
        when(invoiceOpenAmountApi.findOpenAmount("corp-001", 10L)).thenReturn(
            new PurchaseInvoiceOpenAmountApi.PurchaseInvoiceOpenAmount(10L, 20L, new BigDecimal("100.00"),
                new BigDecimal("35.00"), new BigDecimal("65.00"), "POSTED", 2)
        );
        PayableQueryAppServiceImpl service = new PayableQueryAppServiceImpl(
            mock(PayableRepository.class),
            mock(ListValueRenderer.class),
            invoiceOpenAmountApi,
            mock(BizNoGenerator.class)
        );
        SettlementSelectionFillDTO dto = new SettlementSelectionFillDTO();
        dto.setCorpid("corp-001");
        dto.setFieldAttr("main.sourceInvoiceId");
        dto.setReferenceId(10L);

        assertEquals(new BigDecimal("65.00"), service.selectionFill(dto).getPatch().get("main.amount"));
    }
}
