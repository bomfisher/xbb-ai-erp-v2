package xbb.ai.erp.module.sales.application.service.save;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.enums.DocumentStatusEnum;
import xbb.ai.erp.base.common.enums.ApprovalStatusEnum;
import xbb.ai.erp.base.common.enums.OutboundStatusEnum;
import xbb.ai.erp.base.common.enums.ReceiptStatusEnum;
import xbb.ai.erp.base.common.enums.InvoiceStatusEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSaveDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderSubmitSaveDTO;
import xbb.ai.erp.module.sales.application.assembler.SalesOrderAdminAssembler;
import xbb.ai.erp.module.sales.application.validator.SalesOrderValidator;
import xbb.ai.erp.module.sales.application.port.SalesOrderDraftRepository;
import xbb.ai.erp.module.sales.application.validator.SalesOrderSaveProtocolValidator;
import xbb.ai.erp.module.sales.application.validator.SalesOrderSaveCommonValidator;
import xbb.ai.erp.module.sales.application.validator.SalesOrderSaveBusinessValidator;
import xbb.ai.erp.module.sales.application.approval.SalesOrderApprovalSubmitService;
import xbb.ai.erp.module.approval.contract.ApprovalStatus;
import xbb.ai.erp.module.approval.contract.ApprovalSubmission;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderItemRepository;
import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;
import xbb.ai.erp.module.sales.admin.dto.SalesOrderItemDTO;
import xbb.ai.erp.module.inventory.contract.InventoryCommandApi;
import xbb.ai.erp.module.inventory.contract.ReservationCommand;
import xbb.ai.erp.module.inventory.contract.ReservationLine;
import java.time.LocalDateTime;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class SalesOrderSaveAppServiceImpl {

    private final SalesOrderRepository salesOrderRepository;
    private final SalesOrderItemRepository salesOrderItemRepository;
    private final InventoryCommandApi inventoryCommandApi;

    private final SalesOrderDraftRepository draftRepository;
    private final SalesOrderSaveProtocolValidator protocolValidator;
    private final SalesOrderSaveCommonValidator commonValidator;
    private final SalesOrderSaveBusinessValidator businessValidator;
    private final SalesOrderApprovalSubmitService approvalSubmitService;

    @Transactional
    public BaseVO saveAndSubmit(SalesOrderSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        dto.getMain().setTotalAmount(calculateTotalAmount(dto.getItems()));
        Long orderId = save(dto);
        List<SalesOrderItem> items = IntStream.range(0, dto.getItems().size())
            .mapToObj(index -> toItem(dto.getItems().get(index), dto, orderId, index + 1))
            .toList();
        salesOrderItemRepository.insertBatch(items);
        ApprovalSubmission submission = approvalSubmitService.submitCreate(orderId, dto);
        updateApprovalStatus(dto.getCorpid(), orderId, submission.status());
        if (submission.status() == ApprovalStatus.NO_APPROVAL) {
            reserveOrderStock(dto.getCorpid(), orderId, dto.getUserId());
        }
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    @Transactional
    public void activateApprovedOrder(String corpid, Long orderId, String instanceId) {
        SalesOrder order = requireOrder(corpid, orderId);
        if (ApprovalStatusEnum.APPROVED.getCode().equals(order.getAuditStatus())) {
            return;
        }
        order.setAuditStatus(ApprovalStatusEnum.APPROVED.getCode());
        order.setModifyId(instanceId);
        salesOrderRepository.update(order);
        reserveOrderStock(corpid, orderId, instanceId);
    }

    @Transactional
    public void finishUnapprovedOrder(String corpid, Long orderId) {
        SalesOrder order = requireOrder(corpid, orderId);
        if (ApprovalStatusEnum.PENDING.getCode().equals(order.getAuditStatus())
            || ApprovalStatusEnum.PROCESSING.getCode().equals(order.getAuditStatus())) {
            order.setAuditStatus(ApprovalStatusEnum.REJECTED.getCode());
            salesOrderRepository.update(order);
        }
    }

    public Long save(SalesOrderSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        SalesOrderValidator.validateSave(dto);
        SalesOrder entity = SalesOrderAdminAssembler.toSalesOrder(dto);
        if (entity.getId() == null) {
            entity.setStatus(DocumentStatusEnum.OPEN.getCode());
            entity.setAuditStatus(ApprovalStatusEnum.PENDING.getCode());
            entity.setOutboundStatus(OutboundStatusEnum.NOT_OUTBOUNDED.getCode());
            entity.setReceiptStatus(ReceiptStatusEnum.NOT_RECEIVED.getCode());
            entity.setInvoiceStatus(InvoiceStatusEnum.NOT_INVOICED.getCode());
            return salesOrderRepository.insert(entity);
        }
        salesOrderRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            salesOrderRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }

    @Transactional
    public BaseVO audit(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesOrder order = requireOrder(dto.getCorpid(), dto.getId());
        Integer currentStatus = order.getAuditStatus();
        if (!ApprovalStatusEnum.PENDING.getCode().equals(currentStatus)
            && !ApprovalStatusEnum.REJECTED.getCode().equals(currentStatus)) {
            throw new xbb.ai.erp.base.common.exception.BizException("当前销售订单不可审核");
        }
        order.setAuditStatus(ApprovalStatusEnum.APPROVED.getCode());
        order.setModifyId(dto.getUserId());
        salesOrderRepository.update(order);
        return new BaseVO();
    }

    @Transactional
    public BaseVO unaudit(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        SalesOrder order = requireOrder(dto.getCorpid(), dto.getId());
        if (!ApprovalStatusEnum.APPROVED.getCode().equals(order.getAuditStatus())) {
            throw new xbb.ai.erp.base.common.exception.BizException("当前销售订单不可反审核");
        }
        if (!OutboundStatusEnum.NOT_OUTBOUNDED.getCode().equals(order.getOutboundStatus())
            || !ReceiptStatusEnum.NOT_RECEIVED.getCode().equals(order.getReceiptStatus())) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售订单已有出库或收款下游单据，不能反审核");
        }
        boolean hasDeliveredItem = salesOrderItemRepository.findByCondition(
                java.util.Map.of("corpid", order.getCorpid(), "salesOrderId", order.getId())).stream()
            .anyMatch(item -> item.getDeliveredQty() != null && item.getDeliveredQty().signum() > 0);
        if (hasDeliveredItem) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售订单已有出库下游单据，不能反审核");
        }
        order.setAuditStatus(ApprovalStatusEnum.PENDING.getCode());
        order.setModifyId(dto.getUserId());
        salesOrderRepository.update(order);
        return new BaseVO();
    }

    private SalesOrder requireOrder(String corpid, Long id) {
        SalesOrder order = salesOrderRepository.findById(corpid, id);
        if (order == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售订单不存在");
        }
        return order;
    }

    private void updateApprovalStatus(String corpid, Long orderId, ApprovalStatus approvalStatus) {
        SalesOrder order = requireOrder(corpid, orderId);
        if (approvalStatus == ApprovalStatus.NO_APPROVAL) {
            order.setAuditStatus(ApprovalStatusEnum.NO_NEED_APPROVED.getCode());
        } else if (approvalStatus == ApprovalStatus.APPROVED) {
            order.setAuditStatus(ApprovalStatusEnum.APPROVED.getCode());
        } else {
            order.setAuditStatus(ApprovalStatusEnum.PENDING.getCode());
        }
        salesOrderRepository.update(order);
    }

    @Transactional
    public void markOrderApprovalProcessing(String corpid, Long orderId, String instanceId) {
        SalesOrder order = requireOrder(corpid, orderId);
        if (ApprovalStatusEnum.PENDING.getCode().equals(order.getAuditStatus())) {
            order.setAuditStatus(ApprovalStatusEnum.PROCESSING.getCode());
            order.setModifyId(instanceId);
            salesOrderRepository.update(order);
        }
    }

    private void reserveOrderStock(String corpid, Long orderId, String operatorId) {
        List<ReservationLine> reservationLines = salesOrderItemRepository.findByCondition(
                java.util.Map.of("corpid", corpid, "salesOrderId", orderId)).stream()
            .filter(item -> item.getWarehouseId() != null)
            .map(item -> new ReservationLine(item.getWarehouseId(), item.getSkuId(), item.getQty(), item.getId()))
            .toList();
        if (!reservationLines.isEmpty()) {
            inventoryCommandApi.reserve(new ReservationCommand(corpid, BusinessCodeEnum.SALES_ORDER.getCode(), orderId,
                BusinessCodeEnum.SALES_ORDER.getCode(), reservationLines, operatorId, LocalDateTime.now(), "sales-order-" + orderId));
        }
    }

    private static SalesOrderItem toItem(SalesOrderItemDTO dto, SalesOrderSaveDTO request, Long orderId, int lineNo) {
        if (dto.getSkuId() == null || dto.getQty() == null || dto.getQty().signum() <= 0 || dto.getUnitPrice() == null) {
            throw new xbb.ai.erp.base.common.exception.BizException("销售订单明细数据不完整");
        }
        SalesOrderItem item = new SalesOrderItem();
        item.setCorpid(request.getCorpid()); item.setSalesOrderId(orderId); item.setLineNo(lineNo); item.setSkuId(dto.getSkuId()); item.setSkuCode(dto.getSkuCode()); item.setSkuName(dto.getSkuName());
        item.setWarehouseId(dto.getWarehouseId());
        item.setSpecification(dto.getSpecification()); item.setUnitName(dto.getUnitName()); item.setQty(dto.getQty()); item.setDeliveredQty(java.math.BigDecimal.ZERO);
        item.setUnitPrice(dto.getUnitPrice()); item.setTaxRate(dto.getTaxRate() == null ? java.math.BigDecimal.ZERO : dto.getTaxRate());
        item.setAmount(dto.getQty().multiply(dto.getUnitPrice()).setScale(2, RoundingMode.HALF_UP)); item.setCreatorId(request.getUserId()); item.setModifyId(request.getUserId());
        return item;
    }

    private static BigDecimal calculateTotalAmount(List<SalesOrderItemDTO> items) {
        return items.stream()
            .filter(item -> item.getQty() != null && item.getUnitPrice() != null)
            .map(item -> item.getQty().multiply(item.getUnitPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add)
            .setScale(2, RoundingMode.HALF_UP);
    }
}
