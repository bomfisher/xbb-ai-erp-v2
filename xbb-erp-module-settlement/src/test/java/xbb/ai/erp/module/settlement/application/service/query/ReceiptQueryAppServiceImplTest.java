package xbb.ai.erp.module.settlement.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.masterdata.contract.FundAccountReferenceQueryApi;
import xbb.ai.erp.module.settlement.application.field.ReceiptFieldFactory;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptRepository;

class ReceiptQueryAppServiceImplTest {

    @Test
    void shouldPreselectDefaultFundAccountForNewReceipt() {
        FundAccountReferenceQueryApi fundAccountReferenceQueryApi = mock(FundAccountReferenceQueryApi.class);
        when(fundAccountReferenceQueryApi.findDefaultEnabledAccount("corp-001"))
            .thenReturn(new FundAccountReferenceQueryApi.FundAccountReference(1001L, "FA-001", "现金账户", "CASH"));
        ReceiptFieldFactory fieldFactory = mock(ReceiptFieldFactory.class);
        when(fieldFactory.getFields(org.mockito.ArgumentMatchers.any())).thenReturn(List.of());
        BizNoGenerator bizNoGenerator = mock(BizNoGenerator.class);
        when(bizNoGenerator.next("corp-001", "RECEIPT")).thenReturn("SK-202608180001");
        ReceiptQueryAppServiceImpl service = new ReceiptQueryAppServiceImpl(
            mock(ReceiptRepository.class), fieldFactory, null, null, bizNoGenerator, fundAccountReferenceQueryApi);
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-001");

        assertEquals(1001L, service.addItem(dto).getData().getPayments().getFirst().getBankAccountId());
        assertEquals("CASH", service.addItem(dto).getData().getPayments().getFirst().getPaymentMethod());
    }
}
