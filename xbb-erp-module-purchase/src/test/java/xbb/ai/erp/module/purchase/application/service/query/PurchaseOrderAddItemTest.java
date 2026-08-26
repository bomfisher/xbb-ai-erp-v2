package xbb.ai.erp.module.purchase.application.service.query;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import xbb.ai.erp.base.bizno.BizNoGenerator;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.module.purchase.application.field.PurchaseOrderFieldFactory;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

class PurchaseOrderAddItemTest {

    @Test
    void should_generate_purchase_order_number_for_new_item() {
        BizNoGenerator generator = Mockito.mock(BizNoGenerator.class);
        Mockito.when(generator.next("corp-a", "PURCHASE_ORDER")).thenReturn("PO-20260813-00001");
        PurchaseOrderFieldFactory fieldFactory = Mockito.mock(PurchaseOrderFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        PurchaseOrderQueryAppServiceImpl service = new PurchaseOrderQueryAppServiceImpl(
            Mockito.mock(PurchaseOrderRepository.class), Mockito.mock(PurchaseOrderItemRepository.class),
            fieldFactory, null, null, generator);
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-a");

        assertEquals("PO-20260813-00001", service.addItem(dto).getData().getMain().getOrderNo());
        Mockito.verify(generator).next("corp-a", "PURCHASE_ORDER");
    }

    @Test
    void should_return_form_sections_for_new_item() {
        BizNoGenerator generator = Mockito.mock(BizNoGenerator.class);
        PurchaseOrderFieldFactory fieldFactory = Mockito.mock(PurchaseOrderFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        PurchaseOrderQueryAppServiceImpl service = new PurchaseOrderQueryAppServiceImpl(
            Mockito.mock(PurchaseOrderRepository.class), Mockito.mock(PurchaseOrderItemRepository.class),
            fieldFactory, null, null, generator);
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-a");

        assertEquals(List.of("basic", "items", "amount", "remark"), service.addItem(dto).getFormSections().stream().map(section -> section.getKey()).toList());
    }

    @Test
    void should_return_item_stock_linkage_for_new_item() {
        PurchaseOrderFieldFactory fieldFactory = Mockito.mock(PurchaseOrderFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        PurchaseOrderQueryAppServiceImpl service = new PurchaseOrderQueryAppServiceImpl(
            Mockito.mock(PurchaseOrderRepository.class), Mockito.mock(PurchaseOrderItemRepository.class),
            fieldFactory, null, null, Mockito.mock(BizNoGenerator.class));
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-a");

        Map<String, Object> linkageConfig = service.addItem(dto).getLinkageConfig();

        assertNotNull(linkageConfig);
        assertEquals("currentStock", ((Map<?, ?>) linkageConfig.get("itemStock")).get("stockAttr"));
    }

    @Test
    void should_return_product_amount_linkage_for_new_item() {
        PurchaseOrderFieldFactory fieldFactory = Mockito.mock(PurchaseOrderFieldFactory.class);
        Mockito.when(fieldFactory.getFields(Mockito.any())).thenReturn(List.of());
        PurchaseOrderQueryAppServiceImpl service = new PurchaseOrderQueryAppServiceImpl(
            Mockito.mock(PurchaseOrderRepository.class), Mockito.mock(PurchaseOrderItemRepository.class),
            fieldFactory, null, null, Mockito.mock(BizNoGenerator.class));
        BaseDTO dto = new BaseDTO();
        dto.setCorpid("corp-a");

        Map<String, Object> linkageConfig = service.addItem(dto).getLinkageConfig();

        assertEquals("qty", ((Map<?, ?>) linkageConfig.get("rowAmount")).get("quantityAttr"));
        assertEquals("unitPrice", ((Map<?, ?>) linkageConfig.get("rowAmount")).get("unitPriceAttr"));
        assertEquals("main.totalAmount", ((Map<?, ?>) linkageConfig.get("aggregateAmount")).get("targetAttr"));
    }
}
