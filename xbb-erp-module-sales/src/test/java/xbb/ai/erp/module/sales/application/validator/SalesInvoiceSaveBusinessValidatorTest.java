package xbb.ai.erp.module.sales.application.validator;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceLineDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceMainDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesInvoiceSaveDTO;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLine;
import xbb.ai.erp.module.sales.domain.model.SalesInvoiceLineSource;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;
import xbb.ai.erp.module.sales.domain.model.SalesOutbound;
import xbb.ai.erp.module.sales.domain.model.SalesOutboundItem;
import xbb.ai.erp.module.sales.domain.repository.SalesInvoiceLineSourceRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderItemRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundItemRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundRepository;

class SalesInvoiceSaveBusinessValidatorTest {

    private final SalesOrderRepository salesOrderRepository = mock(SalesOrderRepository.class);
    private final SalesOrderItemRepository salesOrderItemRepository = mock(SalesOrderItemRepository.class);
    private final SalesOutboundRepository salesOutboundRepository = mock(SalesOutboundRepository.class);
    private final SalesOutboundItemRepository salesOutboundItemRepository = mock(SalesOutboundItemRepository.class);
    private final SalesInvoiceLineSourceRepository sourceRepository = mock(SalesInvoiceLineSourceRepository.class);
    private final SalesInvoiceSaveBusinessValidator validator = new SalesInvoiceSaveBusinessValidator(
        salesOrderRepository, salesOrderItemRepository, salesOutboundRepository,
        salesOutboundItemRepository, sourceRepository);

    @Test
    void shouldRejectQuantityBeyondPostedInvoiceUsage() {
        SalesOrder order = approvedOrder(10L, 20L);
        SalesOrderItem item = orderItem(30L, 10L, 100L, "源单产品", "10");
        when(salesOrderRepository.findById("corp-a", 10L)).thenReturn(order);
        when(salesOrderItemRepository.findByIdForUpdate("corp-a", 30L)).thenReturn(item);
        when(sourceRepository.sumPostedQuantityBySalesOrderItem("corp-a", 30L, null))
            .thenReturn(new BigDecimal("7"));

        assertThrows(BizException.class, () -> validator.validateForSubmit(
            invoice(line("SALES_ORDER", 10L, 30L, 100L, "源单产品", "4"))));
    }

    @Test
    void shouldRejectOutboundInvoiceWhenRelatedOrderCapacityIsAlreadyUsed() {
        SalesOutbound outbound = approvedOutbound(11L, 20L);
        SalesOutboundItem outboundItem = outboundItem(31L, 11L, 30L, 100L, "源单产品", "6");
        SalesOrderItem orderItem = orderItem(30L, 10L, 100L, "源单产品", "10");
        when(salesOutboundRepository.findById("corp-a", 11L)).thenReturn(outbound);
        when(salesOutboundItemRepository.findByIdForUpdate("corp-a", 31L)).thenReturn(outboundItem);
        when(salesOrderItemRepository.findByIdForUpdate("corp-a", 30L)).thenReturn(orderItem);
        when(sourceRepository.sumPostedQuantity("corp-a", "SALES_OUTBOUND", 31L, null))
            .thenReturn(BigDecimal.ZERO);
        when(sourceRepository.sumPostedQuantityBySalesOrderItem("corp-a", 30L, null))
            .thenReturn(new BigDecimal("7"));

        assertThrows(BizException.class, () -> validator.validateForSubmit(
            invoice(line("SALES_OUTBOUND", 11L, 31L, 100L, "源单产品", "4"))));
    }

    @Test
    void shouldRejectPostWhenOtherInvoiceAlreadyUsedRemainingQuantity() {
        SalesOrder order = approvedOrder(10L, 20L);
        SalesOrderItem item = orderItem(30L, 10L, 100L, "源单产品", "10");
        when(salesOrderRepository.findById("corp-a", 10L)).thenReturn(order);
        when(salesOrderItemRepository.findByIdForUpdate("corp-a", 30L)).thenReturn(item);
        when(sourceRepository.sumPostedQuantityBySalesOrderItem("corp-a", 30L, null))
            .thenReturn(new BigDecimal("7"));

        SalesInvoiceLine invoiceLine = new SalesInvoiceLine();
        invoiceLine.setId(40L);
        invoiceLine.setProductId(100L);
        invoiceLine.setProductName("源单产品");
        invoiceLine.setQuantity(new BigDecimal("4"));
        SalesInvoiceLineSource source = new SalesInvoiceLineSource();
        source.setSalesInvoiceLineId(40L);
        source.setSourceType("SALES_ORDER");
        source.setSourceId(10L);
        source.setSourceLineId(30L);

        assertThrows(BizException.class, () -> validator.validateForPost(
            "corp-a", 20L, List.of(invoiceLine), List.of(source)));
    }

    private SalesInvoiceSaveDTO invoice(SalesInvoiceLineDTO line) {
        SalesInvoiceMainDTO main = new SalesInvoiceMainDTO();
        main.setCustomerId(20L);
        SalesInvoiceSaveDTO dto = new SalesInvoiceSaveDTO();
        dto.setCorpid("corp-a");
        dto.setMain(main);
        dto.setLines(List.of(line));
        return dto;
    }

    private SalesInvoiceLineDTO line(String sourceType, Long sourceId, Long sourceLineId,
                                     Long productId, String productName, String quantity) {
        SalesInvoiceLineDTO line = new SalesInvoiceLineDTO();
        line.setSourceType(sourceType);
        line.setSourceId(sourceId);
        line.setSourceLineId(sourceLineId);
        line.setProductId(productId);
        line.setProductName(productName);
        line.setQuantity(new BigDecimal(quantity));
        return line;
    }

    private SalesOrder approvedOrder(Long id, Long customerId) {
        SalesOrder order = new SalesOrder();
        order.setId(id);
        order.setCustomerId(customerId);
        order.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        return order;
    }

    private SalesOutbound approvedOutbound(Long id, Long customerId) {
        SalesOutbound outbound = new SalesOutbound();
        outbound.setId(id);
        outbound.setCustomerId(customerId);
        outbound.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        return outbound;
    }

    private SalesOrderItem orderItem(Long id, Long orderId, Long skuId, String skuName, String quantity) {
        SalesOrderItem item = new SalesOrderItem();
        item.setId(id);
        item.setSalesOrderId(orderId);
        item.setSkuId(skuId);
        item.setSkuName(skuName);
        item.setQty(new BigDecimal(quantity));
        return item;
    }

    private SalesOutboundItem outboundItem(Long id, Long outboundId, Long orderItemId,
                                           Long skuId, String skuName, String quantity) {
        SalesOutboundItem item = new SalesOutboundItem();
        item.setId(id);
        item.setSalesOutboundId(outboundId);
        item.setSalesOrderItemId(orderItemId);
        item.setSkuId(skuId);
        item.setSkuName(skuName);
        item.setQty(new BigDecimal(quantity));
        return item;
    }
}
