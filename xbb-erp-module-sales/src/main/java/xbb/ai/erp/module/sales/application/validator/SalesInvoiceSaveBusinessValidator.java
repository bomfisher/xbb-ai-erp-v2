package xbb.ai.erp.module.sales.application.validator;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.sales.admin.SalesInvoiceSourceTypeEnum;
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

@Component
@RequiredArgsConstructor
public class SalesInvoiceSaveBusinessValidator {
    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final SalesOutboundRepository salesOutboundRepository;
    private final SalesOutboundItemRepository salesOutboundItemRepository;
    private final SalesInvoiceLineSourceRepository salesInvoiceLineSourceRepository;

    public void validateForSubmit(SalesInvoiceSaveDTO dto) {
        if (dto.getLines() == null) {
            return;
        }
        Map<String, BigDecimal> currentRequestedQuantities = new HashMap<>();
        dto.getLines().stream()
            .filter(line -> SalesInvoiceSourceTypeEnum.isDocumentSource(line.getSourceType()))
            .forEach(line -> validateSourceLine(dto, line, currentRequestedQuantities));
    }

    public void validateForPost(String corpid, Long customerId, List<SalesInvoiceLine> lines,
                                List<SalesInvoiceLineSource> sources) {
        Map<Long, SalesInvoiceLineSource> sourceByInvoiceLineId = new HashMap<>();
        sources.forEach(source -> sourceByInvoiceLineId.put(source.getSalesInvoiceLineId(), source));
        SalesInvoiceMainDTO main = new SalesInvoiceMainDTO();
        main.setCustomerId(customerId);
        SalesInvoiceSaveDTO dto = new SalesInvoiceSaveDTO();
        dto.setCorpid(corpid);
        dto.setMain(main);
        dto.setLines(lines.stream().map(line -> toSourceLineDTO(line, sourceByInvoiceLineId.get(line.getId())))
            .toList());
        validateForSubmit(dto);
    }

    private SalesInvoiceLineDTO toSourceLineDTO(SalesInvoiceLine line, SalesInvoiceLineSource source) {
        SalesInvoiceLineDTO dto = new SalesInvoiceLineDTO();
        dto.setProductId(line.getProductId());
        dto.setProductName(line.getProductName());
        dto.setQuantity(line.getQuantity());
        if (source != null) {
            dto.setSourceType(source.getSourceType());
            dto.setSourceId(source.getSourceId());
            dto.setSourceLineId(source.getSourceLineId());
        }
        return dto;
    }

    private void validateSourceLine(SalesInvoiceSaveDTO dto, SalesInvoiceLineDTO line,
                                    Map<String, BigDecimal> currentRequestedQuantities) {
        if (line.getSourceId() == null || line.getSourceLineId() == null) {
            throw new BizException("来源产品缺少来源单据或来源行");
        }
        if (SalesInvoiceSourceTypeEnum.SALES_ORDER.name().equals(line.getSourceType())) {
            validateSalesOrderSource(dto, line, currentRequestedQuantities);
            return;
        }
        if (SalesInvoiceSourceTypeEnum.SALES_OUTBOUND.name().equals(line.getSourceType())) {
            validateSalesOutboundSource(dto, line, currentRequestedQuantities);
        }
    }

    private void validateSalesOrderSource(SalesInvoiceSaveDTO dto, SalesInvoiceLineDTO line,
                                          Map<String, BigDecimal> currentRequestedQuantities) {
        SalesOrder order = salesOrderRepository.findById(dto.getCorpid(), line.getSourceId());
        if (order == null || !AuditStatusEnum.allowsDownstream(order.getAuditStatus())) {
            throw new BizException("销售订单不存在或未审核");
        }
        validateCustomer(dto, order.getCustomerId());
        SalesOrderItem sourceItem = salesOrderItemRepository.findByIdForUpdate(dto.getCorpid(),
            line.getSourceLineId());
        if (sourceItem == null || !Objects.equals(sourceItem.getSalesOrderId(), order.getId())) {
            throw new BizException("来源产品不属于所选销售订单");
        }
        validateSourceProduct(line, sourceItem.getSkuId(), sourceItem.getSkuName());
        validateAvailableQuantity(line, sourceItem.getQty(), salesInvoiceLineSourceRepository
            .sumPostedQuantityBySalesOrderItem(dto.getCorpid(), sourceItem.getId(), currentInvoiceId(dto)),
            "SALES_ORDER:" + sourceItem.getId(), currentRequestedQuantities);
    }

    private void validateSalesOutboundSource(SalesInvoiceSaveDTO dto, SalesInvoiceLineDTO line,
                                             Map<String, BigDecimal> currentRequestedQuantities) {
        SalesOutbound outbound = salesOutboundRepository.findById(dto.getCorpid(), line.getSourceId());
        if (outbound == null || !AuditStatusEnum.allowsDownstream(outbound.getAuditStatus())) {
            throw new BizException("销售出库单不存在或未审核");
        }
        validateCustomer(dto, outbound.getCustomerId());
        SalesOutboundItem sourceItem = salesOutboundItemRepository.findByIdForUpdate(dto.getCorpid(),
            line.getSourceLineId());
        if (sourceItem == null || !Objects.equals(sourceItem.getSalesOutboundId(), outbound.getId())) {
            throw new BizException("来源产品不属于所选销售出库单");
        }
        validateSourceProduct(line, sourceItem.getSkuId(), sourceItem.getSkuName());
        if (sourceItem.getSalesOrderItemId() == null) {
            validateAvailableQuantity(line, sourceItem.getQty(), salesInvoiceLineSourceRepository
                .sumPostedQuantity(dto.getCorpid(), line.getSourceType(), sourceItem.getId(),
                    currentInvoiceId(dto)),
                "SALES_OUTBOUND:" + sourceItem.getId(), currentRequestedQuantities);
            return;
        }
        SalesOrderItem orderItem = salesOrderItemRepository.findByIdForUpdate(
            dto.getCorpid(), sourceItem.getSalesOrderItemId());
        if (orderItem == null) {
            throw new BizException("销售出库来源缺少对应销售订单产品");
        }
        validateAvailableQuantity(line, sourceItem.getQty(), salesInvoiceLineSourceRepository
            .sumPostedQuantity(dto.getCorpid(), line.getSourceType(), sourceItem.getId(),
                currentInvoiceId(dto)),
            "SALES_OUTBOUND:" + sourceItem.getId(), currentRequestedQuantities);
        validateAvailableQuantity(line, orderItem.getQty(), salesInvoiceLineSourceRepository
            .sumPostedQuantityBySalesOrderItem(dto.getCorpid(), orderItem.getId(), currentInvoiceId(dto)),
            "SALES_ORDER:" + orderItem.getId(), currentRequestedQuantities);
    }

    private void validateCustomer(SalesInvoiceSaveDTO dto, Long sourceCustomerId) {
        if (dto.getMain().getCustomerId() == null
            || !Objects.equals(dto.getMain().getCustomerId(), sourceCustomerId)) {
            throw new BizException("来源单据客户与销售发票客户不一致");
        }
    }

    private Long currentInvoiceId(SalesInvoiceSaveDTO dto) {
        return dto.getMain() == null ? null : dto.getMain().getId();
    }

    private void validateSourceProduct(SalesInvoiceLineDTO line, Long sourceProductId, String sourceProductName) {
        if (!Objects.equals(line.getProductId(), sourceProductId)
            || !Objects.equals(line.getProductName(), sourceProductName)) {
            throw new BizException("发票产品必须与来源单据产品一致");
        }
    }

    private void validateAvailableQuantity(SalesInvoiceLineDTO line, BigDecimal sourceQuantity,
                                           BigDecimal postedQuantity, String capacityKey,
                                           Map<String, BigDecimal> currentRequestedQuantities) {
        BigDecimal requestedQuantity = line.getQuantity();
        if (sourceQuantity == null || requestedQuantity == null) {
            throw new BizException("来源产品数量不能为空");
        }
        BigDecimal currentRequestedQuantity = currentRequestedQuantities.getOrDefault(capacityKey, BigDecimal.ZERO);
        BigDecimal availableQuantity = sourceQuantity.subtract(postedQuantity).subtract(currentRequestedQuantity);
        if (requestedQuantity.compareTo(availableQuantity) > 0) {
            throw new BizException("来源产品可开票数量不足，来源数量："
                + sourceQuantity + "，已开票数量：" + postedQuantity.add(currentRequestedQuantity)
                + "，可开票数量：" + availableQuantity);
        }
        currentRequestedQuantities.merge(capacityKey, requestedQuantity, BigDecimal::add);
    }
}
