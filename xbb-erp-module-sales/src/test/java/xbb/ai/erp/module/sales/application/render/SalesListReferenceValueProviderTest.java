package xbb.ai.erp.module.sales.application.render;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Set;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.module.sales.domain.model.SalesInvoice;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;

class SalesListReferenceValueProviderTest {
  @Test
  void shouldRenderOrderAndInvoiceNumbers() {
    SalesOrder order = new SalesOrder();
    order.setId(1L);
    order.setOrderNo("SO-001");
    SalesInvoice invoice = new SalesInvoice();
    invoice.setId(2L);
    invoice.setInvoiceNo("SI-002");
    SalesOrderRepository orderRepository = mock(SalesOrderRepository.class);
    SalesInvoiceRepository invoiceRepository = mock(SalesInvoiceRepository.class);
    when(orderRepository.findByIds("corp-001", Set.of(1L))).thenReturn(List.of(order));
    when(invoiceRepository.findByIds("corp-001", Set.of(2L))).thenReturn(List.of(invoice));

    assertEquals(
        "SO-001",
        new SalesOrderListReferenceValueProvider(orderRepository)
            .findDisplayMap("corp-001", Set.of("1"))
            .get("1"));
    assertEquals(
        "SI-002",
        new SalesInvoiceListReferenceValueProvider(invoiceRepository)
            .findDisplayMap("corp-001", Set.of("2"))
            .get("2"));
  }
}
