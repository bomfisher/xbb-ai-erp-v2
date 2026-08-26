package xbb.ai.erp.module.sales.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.enums.OutboundStatusEnum;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSubmitSaveDTO;
import xbb.ai.erp.module.sales.application.assembler.SalesOutboundAdminAssembler;
import xbb.ai.erp.module.sales.application.validator.SalesOutboundValidator;
import xbb.ai.erp.module.sales.application.port.SalesOutboundDraftRepository;
import xbb.ai.erp.module.sales.application.validator.SalesOutboundSaveProtocolValidator;
import xbb.ai.erp.module.sales.application.validator.SalesOutboundSaveCommonValidator;
import xbb.ai.erp.module.sales.application.validator.SalesOutboundSaveBusinessValidator;
import xbb.ai.erp.module.sales.domain.model.SalesOutbound;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundRepository;
import xbb.ai.erp.module.sales.domain.model.SalesOutboundItem;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundItemRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderItemRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;
import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundItemDTO;
import xbb.ai.erp.module.inventory.contract.InventoryCommandApi;
import xbb.ai.erp.module.inventory.contract.OutboundCommand;
import xbb.ai.erp.module.inventory.contract.OutboundLine;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SalesOutboundSaveAppServiceImpl {

    private final SalesOutboundRepository salesOutboundRepository;
    private final SalesOutboundItemRepository salesOutboundItemRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final SalesOrderRepository salesOrderRepository;
    private final InventoryCommandApi inventoryCommandApi;

    private final SalesOutboundDraftRepository draftRepository;
    private final SalesOutboundSaveProtocolValidator protocolValidator;
    private final SalesOutboundSaveCommonValidator commonValidator;
    private final SalesOutboundSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(SalesOutboundSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        SalesOrder salesOrder = validateSourceOrder(dto);
        List<SalesOutboundItem> items = dto.getItems().stream().map(item -> toItem(item, dto, null)).toList();
        if (items.isEmpty()) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售出库明细不能为空");
        }
        inventoryCommandApi.validateOutbound(new OutboundCommand(dto.getCorpid(), BusinessCodeEnum.SALES_OUTBOUND.getCode(),
            salesOrder.getId(), BusinessCodeEnum.SALES_ORDER.getCode(), toOutboundLines(items),
            dto.getUserId(), LocalDateTime.now(), "sales-outbound-validation-" + salesOrder.getId()));
        Long outboundId = save(dto);
        items.forEach(item -> item.setSalesOutboundId(outboundId));
        salesOutboundItemRepository.insertBatch(items);
        String businessCode = BusinessCodeEnum.SALES_OUTBOUND.getCode();
        inventoryCommandApi.postReservedOutbound(new OutboundCommand(dto.getCorpid(), businessCode,
            salesOrder.getId(), BusinessCodeEnum.SALES_ORDER.getCode(), items.stream()
                .map(item -> new OutboundLine(item.getWarehouseId(), item.getSkuId(), item.getQty(), item.getSalesOrderItemId())).toList(),
            dto.getUserId(), LocalDateTime.now(), businessCode + ":" + outboundId + ":POST"));
        for (SalesOutboundItem item : items) {
            SalesOrderItem orderItem = salesOrderItemRepository.findById(dto.getCorpid(), item.getSalesOrderItemId());
            if (orderItem == null) {
                throw new xbb.ai.erp.base.common.exception.BizException("销售订单行不存在");
            }
            java.math.BigDecimal delivered = orderItem.getDeliveredQty() == null ? BigDecimal.ZERO : orderItem.getDeliveredQty();
            delivered = delivered.add(item.getQty());
            if (delivered.compareTo(orderItem.getQty()) > 0) {
                throw new xbb.ai.erp.base.common.exception.BizException("出库数量超过订单待出库数量");
            }
            orderItem.setDeliveredQty(delivered);
            orderItem.setOutboundStatus(delivered.compareTo(orderItem.getQty()) == 0 ? 2 : 1);
            orderItem.setModifyId(dto.getUserId());
            salesOrderItemRepository.update(orderItem);
        }
        updateOrderOutboundStatus(salesOrder, dto.getUserId());
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    private static List<OutboundLine> toOutboundLines(List<SalesOutboundItem> items) {
        return items.stream()
            .map(item -> new OutboundLine(item.getWarehouseId(), item.getSkuId(), item.getQty(), item.getSalesOrderItemId()))
            .toList();
    }

    public Long save(SalesOutboundSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SalesOutboundValidator.validateSave(dto);
        SalesOutbound entity = SalesOutboundAdminAssembler.toSalesOutbound(dto);
        BigDecimal totalAmount = dto.getItems() == null ? BigDecimal.ZERO : dto.getItems().stream()
            .filter(item -> item.getQty() != null && item.getUnitPrice() != null)
            .map(item -> item.getQty().multiply(item.getUnitPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
        entity.setTotalAmount(totalAmount);
        if (entity.getId() == null) {
            entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
            return salesOutboundRepository.insert(entity);
        }
        salesOutboundRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            salesOutboundRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }

    @Transactional
    public BaseVO audit(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesOutbound outbound = requireOutbound(dto.getCorpid(), dto.getId());
        if (AuditStatusEnum.APPROVED.getCode().equals(outbound.getAuditStatus())) {
            throw new BizException("当前销售出库单已审核");
        }
        outbound.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        outbound.setModifyId(dto.getUserId());
        salesOutboundRepository.update(outbound);
        return new BaseVO();
    }

    @Transactional
    public BaseVO unaudit(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesOutbound outbound = requireOutbound(dto.getCorpid(), dto.getId());
        if (!AuditStatusEnum.APPROVED.getCode().equals(outbound.getAuditStatus())) {
            throw new BizException("当前销售出库单不可反审核");
        }
        outbound.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        outbound.setModifyId(dto.getUserId());
        salesOutboundRepository.update(outbound);
        return new BaseVO();
    }

    private SalesOutbound requireOutbound(String corpid, Long id) {
        SalesOutbound outbound = salesOutboundRepository.findById(corpid, id);
        if (outbound == null) {
            throw new BizException("销售出库单不存在");
        }
        return outbound;
    }

    private SalesOutboundItem toItem(SalesOutboundItemDTO dto, SalesOutboundSaveDTO request, Long outboundId) {
        if (dto.getSalesOrderItemId() == null || dto.getSkuId() == null || dto.getQty() == null || dto.getQty().signum() <= 0
            || dto.getUnitPrice() == null || dto.getWarehouseId() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售出库明细数据不完整");
        }
        SalesOrderItem orderItem = salesOrderItemRepository.findById(request.getCorpid(), dto.getSalesOrderItemId());
        if (orderItem == null || request.getMain() == null
            || !request.getMain().getSalesOrderId().equals(orderItem.getSalesOrderId())
            || !dto.getSkuId().equals(orderItem.getSkuId())
            || orderItem.getUnitPrice() == null || dto.getUnitPrice().compareTo(orderItem.getUnitPrice()) != 0) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售出库明细必须来源于销售订单");
        }
        SalesOutboundItem item = new SalesOutboundItem();
        item.setCorpid(request.getCorpid()); item.setSalesOutboundId(outboundId); item.setSalesOrderItemId(dto.getSalesOrderItemId());
        item.setSkuId(dto.getSkuId()); item.setSkuName(dto.getSkuName()); item.setUnitName(dto.getUnitName()); item.setQty(dto.getQty()); item.setUnitPrice(dto.getUnitPrice());
        item.setWarehouseId(dto.getWarehouseId());
        item.setAmount(dto.getQty().multiply(dto.getUnitPrice()).setScale(2, RoundingMode.HALF_UP)); item.setCostUnit(BigDecimal.ZERO); item.setCostAmount(BigDecimal.ZERO);
        item.setOutboundStatus(OutboundStatusEnum.NOT_OUTBOUNDED.getCode());
        item.setCreatorId(request.getUserId());
        item.setModifyId(request.getUserId());
        return item;
    }

    private SalesOrder validateSourceOrder(SalesOutboundSubmitSaveDTO dto) {
        if (dto.getMain() == null || dto.getMain().getSalesOrderId() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售出库必须关联销售订单");
        }
        SalesOrder order = salesOrderRepository.findById(dto.getCorpid(), dto.getMain().getSalesOrderId());
        if (order == null || !AuditStatusEnum.allowsDownstream(order.getAuditStatus())) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售订单不存在");
        }
        if (!OutboundStatusEnum.NOT_OUTBOUNDED.getCode().equals(order.getOutboundStatus())
            && !OutboundStatusEnum.PARTIALLY_OUTBOUNDED.getCode().equals(order.getOutboundStatus())) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售订单已全部出库");
        }
        if (dto.getMain().getCustomerId() != null && !dto.getMain().getCustomerId().equals(order.getCustomerId())) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售出库客户必须与销售订单一致");
        }
        if (dto.getMain().getId() != null) {
            SalesOutbound existing = salesOutboundRepository.findById(dto.getCorpid(), dto.getMain().getId());
            if (existing == null) {
                throw new xbb.ai.erp.base.common.exception.BizException("销售出库单不存在");
            }
            if (!dto.getMain().getSalesOrderId().equals(existing.getSalesOrderId())
                || !order.getCustomerId().equals(existing.getCustomerId())) {
                throw new xbb.ai.erp.base.common.exception.BizException("客户和销售订单不允许修改");
            }
        }
        dto.getMain().setCustomerId(order.getCustomerId());
        return order;
    }

    private void updateOrderOutboundStatus(SalesOrder order, String userId) {
        List<SalesOrderItem> orderItems = salesOrderItemRepository.findByCondition(
            java.util.Map.of("corpid", order.getCorpid(), "salesOrderId", order.getId()));
        boolean hasDelivered = orderItems.stream().anyMatch(item -> item.getDeliveredQty() != null
            && item.getDeliveredQty().signum() > 0);
        boolean allDelivered = !orderItems.isEmpty() && orderItems.stream().allMatch(item -> item.getQty() != null
            && item.getDeliveredQty() != null && item.getDeliveredQty().compareTo(item.getQty()) >= 0);
        order.setOutboundStatus(allDelivered ? OutboundStatusEnum.FULLY_OUTBOUNDED.getCode()
            : hasDelivered ? OutboundStatusEnum.PARTIALLY_OUTBOUNDED.getCode()
            : OutboundStatusEnum.NOT_OUTBOUNDED.getCode());
        order.setModifyId(userId);
        salesOrderRepository.update(order);
    }
}
