package xbb.ai.erp.module.sales.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.inventory.contract.InventoryStockQueryApi;
import xbb.ai.erp.module.sales.application.field.SalesOrderFieldFactory;
import xbb.ai.erp.module.sales.application.field.SalesOutboundFieldFactory;
import xbb.ai.erp.module.sales.application.field.SalesInvoiceFieldFactory;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceLineRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderItemRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundItemRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundRepository;

class SalesAddItemBizNoTest {

    @Test
    void shouldGenerateSalesOrderNumberForNewItem() {
        BizNoGenerator generator = Mockito.mock(BizNoGenerator.class);
        Mockito.when(generator.next("corp-a", "SALES_ORDER")).thenReturn("SO-20260819-00001");
        SalesOrderFieldFactory fieldFactory = Mockito.mock(SalesOrderFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        SalesOrderQueryAppServiceImpl service = new SalesOrderQueryAppServiceImpl(
            Mockito.mock(SalesOrderRepository.class), Mockito.mock(SalesOrderItemRepository.class), fieldFactory,
            null, null, Mockito.mock(InventoryStockQueryApi.class), generator);

        assertEquals("SO-20260819-00001", service.addItem(baseDTO()).getData().getMain().getOrderNo());
        Mockito.verify(generator).next("corp-a", "SALES_ORDER");
    }

    @Test
    void shouldGenerateSalesOutboundNumberForNewItem() {
        BizNoGenerator generator = Mockito.mock(BizNoGenerator.class);
        Mockito.when(generator.next("corp-a", "SALES_OUTBOUND")).thenReturn("SO-20260819-00002");
        SalesOutboundFieldFactory fieldFactory = Mockito.mock(SalesOutboundFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        SalesOutboundQueryAppServiceImpl service = new SalesOutboundQueryAppServiceImpl(
            Mockito.mock(SalesOutboundRepository.class), Mockito.mock(SalesOrderRepository.class),
            Mockito.mock(SalesOrderItemRepository.class), Mockito.mock(SalesOutboundItemRepository.class),
            fieldFactory, null, null, generator);

        assertEquals("SO-20260819-00002", service.addItem(baseDTO()).getData().getMain().getOutboundNo());
        Mockito.verify(generator).next("corp-a", "SALES_OUTBOUND");
    }

    @Test
    void shouldProvideInvoiceAmountLinkageForNewItem() {
        BizNoGenerator generator = Mockito.mock(BizNoGenerator.class);
        Mockito.when(generator.next("corp-a", "SALES_INVOICE")).thenReturn("SI-20260820-00001");
        SalesInvoiceFieldFactory fieldFactory = Mockito.mock(SalesInvoiceFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        SalesInvoiceQueryAppServiceImpl service = new SalesInvoiceQueryAppServiceImpl(
            Mockito.mock(SalesInvoiceRepository.class), Mockito.mock(SalesInvoiceLineRepository.class),
            fieldFactory, null, null, generator);

        assertEquals("invoiceAmount", service.addItem(baseDTO()).getLinkageConfig().keySet().iterator().next());
    }

    private BaseDTO baseDTO() {
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-a");
        return dto;
    }
}
