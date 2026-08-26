package xbb.ai.erp.module.settlement.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.settlement.application.field.ReceivableFieldFactory;
import xbb.ai.erp.module.settlement.domain.repository.ReceivableRepository;
import xbb.ai.erp.module.sales.contract.SalesInvoiceOpenAmountApi;

class ReceivableQueryAppServiceImplTest {

    @Test
    void shouldGenerateReceivableNumberAndInitializeManualSourceType() {
        BizNoGenerator bizNoGenerator = mock(BizNoGenerator.class);
        when(bizNoGenerator.next("corp-001", "RECEIVABLE")).thenReturn("YS-202608180001");
        ReceivableFieldFactory fieldFactory = mock(ReceivableFieldFactory.class);
        when(fieldFactory.getFields(any())).thenReturn(List.of());
        ReceivableQueryAppServiceImpl service = new ReceivableQueryAppServiceImpl(
            mock(ReceivableRepository.class),
            fieldFactory,
            null,
            null,
            bizNoGenerator,
            mock(SalesInvoiceOpenAmountApi.class)
        );
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-001");

        assertEquals("YS-202608180001", service.addItem(dto).getData().getMain().getReceivableNo());
        assertEquals("MANUAL_ADJUSTMENT", service.addItem(dto).getData().getMain().getSourceType());
    }
}
