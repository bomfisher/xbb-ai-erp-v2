package xbb.ai.erp.module.settlement.application.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.settlement.domain.model.Receipt;
import xbb.ai.erp.module.settlement.domain.model.Receivable;
import xbb.ai.erp.module.settlement.domain.repository.ReceiptRepository;
import xbb.ai.erp.module.settlement.domain.repository.ReceivableRepository;

class SettlementListReferenceValueProviderTest {
  @Test
  void shouldRenderAdvanceReceiptAndReceivableNumbers() {
    Receipt advanceReceipt = new Receipt();
    advanceReceipt.setId(1L);
    advanceReceipt.setReceiptNo("AR-001");
    advanceReceipt.setReceiptType("ADVANCE_PAYMENT");
    Receipt standardReceipt = new Receipt();
    standardReceipt.setId(2L);
    standardReceipt.setReceiptNo("R-002");
    standardReceipt.setReceiptType("CUSTOMER_PAYMENT");
    Receivable receivable = new Receivable();
    receivable.setId(3L);
    receivable.setReceivableNo("RV-003");
    ReceiptRepository receiptRepository = mock(ReceiptRepository.class);
    ReceivableRepository receivableRepository = mock(ReceivableRepository.class);
    when(receiptRepository.findByIds("corp-001", Set.of(1L, 2L)))
        .thenReturn(List.of(advanceReceipt, standardReceipt));
    when(receivableRepository.findByIds("corp-001", Set.of(3L))).thenReturn(List.of(receivable));

    assertEquals(
        "AR-001",
        new AdvanceReceiptListReferenceValueProvider(receiptRepository)
            .findDisplayMap("corp-001", Set.of("1", "2"))
            .get("1"));
    assertEquals(
        null,
        new AdvanceReceiptListReferenceValueProvider(receiptRepository)
            .findDisplayMap("corp-001", Set.of("1", "2"))
            .get("2"));
    assertEquals(
        "RV-003",
        new ReceivableListReferenceValueProvider(receivableRepository)
            .findDisplayMap("corp-001", Set.of("3"))
            .get("3"));
  }
}
