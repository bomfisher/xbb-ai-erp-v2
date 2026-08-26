package xbb.ai.erp.module.purchase.application.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

class PurchaseOrderListReferenceValueProviderTest {
  @Test
  void shouldRenderPurchaseOrderNumber() {
    PurchaseOrder order = new PurchaseOrder();
    order.setId(1L);
    order.setOrderNo("PO-001");
    PurchaseOrderRepository repository = mock(PurchaseOrderRepository.class);
    when(repository.findByIds("corp-001", Set.of(1L))).thenReturn(List.of(order));

    assertEquals(
        "PO-001",
        new PurchaseOrderListReferenceValueProvider(repository)
            .findDisplayMap("corp-001", Set.of("1"))
            .get("1"));
  }
}
