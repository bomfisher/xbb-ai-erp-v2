package xbb.ai.erp.module.purchase.application.service.save;

import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.enums.InboundStatusEnum;
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.enums.PurchaseInboundStatusEnum;
import xbb.ai.erp.base.common.module.BusinessCodeEnum;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundItemDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundConfirmDTO;
import xbb.ai.erp.module.purchase.application.port.PurchaseInboundApprovalPolicy;
import xbb.ai.erp.module.inventory.contract.InboundCommand;
import xbb.ai.erp.module.inventory.contract.InboundLine;
import xbb.ai.erp.module.inventory.contract.InventoryCommandApi;
import xbb.ai.erp.base.common.exception.BizException;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseInboundAdminAssembler;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundValidator;
import xbb.ai.erp.module.purchase.application.port.PurchaseInboundDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundSaveProtocolValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseInboundSaveBusinessValidator;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInboundItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundItemRepository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;

@Service
@RequiredArgsConstructor
public class PurchaseInboundSaveAppServiceImpl {

    private final PurchaseInboundRepository purchaseInboundRepository;
    private final PurchaseInboundItemRepository purchaseInboundItemRepository;
    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    private final PurchaseInboundDraftRepository draftRepository;
    private final PurchaseInboundSaveProtocolValidator protocolValidator;
    private final PurchaseInboundSaveCommonValidator commonValidator;
    private final PurchaseInboundSaveBusinessValidator businessValidator;
    private final PurchaseInboundApprovalPolicy approvalPolicy;
    private final InventoryCommandApi inventoryCommandApi;

    @Transactional
    public BaseVO saveAndSubmit(PurchaseInboundSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        rejectPostedInboundModification(dto);
        dto.getMain().setTotalAmount(totalAmount(dto.getItems()));
        dto.getMain().setStatus(PurchaseInboundStatusEnum.SUBMITTED.getCode());
        List<PurchaseInboundItem> existingItems = existingItems(dto);
        Long purchaseInboundId = save(dto);
        syncItems(dto, purchaseInboundId, existingItems);
        updatePurchaseOrderInboundProgress(dto.getMain().getPurchaseOrderId(),
        inboundQtyChanges(existingItems, dto.getItems()), dto.getCorpid(), dto.getUserId());
        if (!approvalPolicy.requiresApproval(dto.getCorpid())) {
            confirmInbound(purchaseInboundId, dto.getCorpid(), dto.getUserId());
        }
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) {
            draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        }
        return new BaseVO();
    }

    /**
     * 供审核通过回调或人工动作调用；只允许已提交且尚未入账的采购入库单确认入库。
     */
    @Transactional
    public BaseVO confirmInbound(PurchaseInboundConfirmDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        if (dto.getId() == null) {
            throw new BizException("采购入库单ID不能为空");
        }
        confirmInbound(dto.getId(), dto.getCorpid(), dto.getUserId());
        return new BaseVO();
    }

    @Transactional
    public BaseVO audit(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInbound inbound = requireInbound(dto.getCorpid(), dto.getId());
        if (!PurchaseInboundStatusEnum.SUBMITTED.getCode().equals(inbound.getStatus())) {
            throw new BizException("当前采购入库单不可审核");
        }
        if (AuditStatusEnum.APPROVED.getCode().equals(inbound.getAuditStatus())) {
            throw new BizException("当前采购入库单已审核");
        }
        inbound.setAuditStatus(AuditStatusEnum.APPROVED.getCode());
        inbound.setModifyId(dto.getUserId());
        inbound.setUpdateTime(System.currentTimeMillis());
        purchaseInboundRepository.update(inbound);
        confirmInbound(inbound.getId(), inbound.getCorpid(), dto.getUserId());
        return new BaseVO();
    }

    @Transactional
    public BaseVO unaudit(xbb.ai.erp.base.common.dto.IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInbound inbound = requireInbound(dto.getCorpid(), dto.getId());
        if (!AuditStatusEnum.APPROVED.getCode().equals(inbound.getAuditStatus())) {
            throw new BizException("当前采购入库单不可反审核");
        }
        if (!PurchaseInboundStatusEnum.SUBMITTED.getCode().equals(inbound.getStatus())) {
            throw new BizException("采购入库单已确认入库，不能反审核");
        }
        inbound.setAuditStatus(AuditStatusEnum.PENDING.getCode());
        inbound.setModifyId(dto.getUserId());
        inbound.setUpdateTime(System.currentTimeMillis());
        purchaseInboundRepository.update(inbound);
        return new BaseVO();
    }

    private void confirmInbound(Long inboundId, String corpid, String userId) {
        PurchaseInbound inbound = requireInbound(corpid, inboundId);
        if (PurchaseInboundStatusEnum.INVENTORY_POSTED.getCode().equals(inbound.getStatus())) {
            return;
        }
        if (!PurchaseInboundStatusEnum.SUBMITTED.getCode().equals(inbound.getStatus())) {
            throw new BizException("采购入库单尚未提交，不能确认入库");
        }
        validateAuditStatus(inbound, corpid);
        List<PurchaseInboundItem> items = purchaseInboundItemRepository.findByCondition(
            Map.of("corpid", corpid, "purchaseInboundId", inboundId));
        if (items.isEmpty()) {
            throw new BizException("采购入库单没有可入账的产品明细");
        }
        List<InboundLine> lines = items.stream().map(item -> new InboundLine(
            item.getWarehouseId(), item.getSkuId(), item.getQty(), item.getCostAmount(), item.getId())).toList();
        String businessCode = BusinessCodeEnum.PURCHASE_INBOUND.getCode();
        inventoryCommandApi.postInbound(new InboundCommand(corpid, businessCode, inboundId, businessCode,
            lines, userId, occurredAt(inbound.getInboundDate()), businessCode + ":" + inboundId + ":POST"));
        inbound.setStatus(PurchaseInboundStatusEnum.INVENTORY_POSTED.getCode());
        inbound.setModifyId(userId);
        inbound.setUpdateTime(System.currentTimeMillis());
        purchaseInboundRepository.update(inbound);
    }

    private PurchaseInbound requireInbound(String corpid, Long id) {
        PurchaseInbound inbound = purchaseInboundRepository.findById(corpid, id);
        if (inbound == null) {
            throw new BizException("采购入库单不存在");
        }
        return inbound;
    }

    private void rejectPostedInboundModification(PurchaseInboundSubmitSaveDTO dto) {
        if (dto.getMain().getId() == null) {
            return;
        }
        PurchaseInbound existing = purchaseInboundRepository.findById(dto.getCorpid(), dto.getMain().getId());
        if (existing != null && PurchaseInboundStatusEnum.INVENTORY_POSTED.getCode().equals(existing.getStatus())) {
            throw new BizException("已确认入库的采购单不可修改，请通过冲销或退货流程处理");
        }
    }

    public Long save(PurchaseInboundSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        PurchaseInboundValidator.validateSave(dto);
        PurchaseInbound entity = PurchaseInboundAdminAssembler.toPurchaseInbound(dto);
        long now = System.currentTimeMillis();
        entity.setModifyId(dto.getUserId());
        entity.setUpdateTime(now);
        if (entity.getId() == null) {
            entity.setCreatorId(dto.getUserId());
            entity.setAddTime(now);
            entity.setDel(0);
            if (entity.getSupplierName() == null || entity.getSupplierName().isBlank()) {
                entity.setSupplierName("MOCK");
            }
            if (entity.getTotalAmount() == null) {
                entity.setTotalAmount(BigDecimal.ZERO);
            }
            entity.setStatus(PurchaseInboundStatusEnum.SUBMITTED.getCode());
            entity.setAuditStatus(AuditStatusEnum.PENDING.getCode());
            return purchaseInboundRepository.insert(entity);
        }
        purchaseInboundRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            purchaseInboundRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }

    private List<PurchaseInboundItem> existingItems(PurchaseInboundSaveDTO dto) {
        if (dto.getMain().getId() == null) {
            return List.of();
        }
        return purchaseInboundItemRepository.findByCondition(
            Map.of("corpid", dto.getCorpid(), "purchaseInboundId", dto.getMain().getId()));
    }

    private void syncItems(PurchaseInboundSaveDTO dto, Long purchaseInboundId, List<PurchaseInboundItem> existingItems) {
        Map<Long, PurchaseInboundItem> existingById = new HashMap<>();
        existingItems.forEach(item -> existingById.put(item.getId(), item));

        Set<Long> submittedIds = new HashSet<>();
        List<PurchaseInboundItem> itemsToInsert = new ArrayList<>();
        List<PurchaseInboundItem> itemsToUpdate = new ArrayList<>();
        for (PurchaseInboundItemDTO itemDto : dto.getItems()) {
            PurchaseInboundItem item = PurchaseInboundAdminAssembler.toPurchaseInboundItem(itemDto, dto.getCorpid(),
                purchaseInboundId, dto.getUserId());
            if (item.getId() == null) {
                itemsToInsert.add(item);
                continue;
            }
            if (!submittedIds.add(item.getId())) {
                throw new xbb.ai.erp.base.common.exception.BizException("入库产品明细ID重复");
            }
            PurchaseInboundItem existing = existingById.get(item.getId());
            if (existing == null) {
                throw new xbb.ai.erp.base.common.exception.BizException("入库产品明细不存在或不属于当前入库单");
            }
            item.setCreatorId(existing.getCreatorId());
            itemsToUpdate.add(item);
        }
        List<Long> idsToRemove = existingById.keySet().stream().filter(id -> !submittedIds.contains(id)).toList();
        if (!idsToRemove.isEmpty()) {
            purchaseInboundItemRepository.removeBatchByIds(dto.getCorpid(), idsToRemove);
        }
        itemsToUpdate.forEach(purchaseInboundItemRepository::update);
        if (!itemsToInsert.isEmpty()) {
            purchaseInboundItemRepository.insertBatch(itemsToInsert);
        }
    }

    private static BigDecimal totalAmount(List<PurchaseInboundItemDTO> items) {
        return items.stream().map(item -> item.getQty().multiply(item.getUnitPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }

    private void validateAuditStatus(PurchaseInbound inbound, String corpid) {
        if (approvalPolicy.requiresApproval(corpid)
            && !AuditStatusEnum.allowsDownstream(inbound.getAuditStatus())) {
            throw new BizException("采购入库单尚未审核通过，不能确认入库");
        }
    }

    private void updatePurchaseOrderInboundProgress(Long purchaseOrderId, Map<Long, BigDecimal> inboundQtyChanges,
                                                    String corpid, String userId) {
        if (inboundQtyChanges.isEmpty()) {
            return;
        }
        PurchaseOrder purchaseOrder = purchaseOrderRepository.findById(corpid, purchaseOrderId);
        if (purchaseOrder == null) {
            throw new BizException("采购订单不存在，不能更新入库进度");
        }
        if (!AuditStatusEnum.allowsDownstream(purchaseOrder.getAuditStatus())) {
            throw new BizException("采购订单尚未审核通过，不能更新入库进度");
        }
        List<PurchaseOrderItem> orderItems = purchaseOrderItemRepository.findByCondition(
            Map.of("corpid", corpid, "purchaseOrderId", purchaseOrder.getId()));
        Map<Long, PurchaseOrderItem> orderItemById = new HashMap<>();
        orderItems.forEach(item -> orderItemById.put(item.getId(), item));
        long now = System.currentTimeMillis();
        for (Map.Entry<Long, BigDecimal> entry : inboundQtyChanges.entrySet()) {
            PurchaseOrderItem orderItem = orderItemById.get(entry.getKey());
            if (orderItem == null || !purchaseOrder.getId().equals(orderItem.getPurchaseOrderId())) {
                throw new BizException("入库产品不属于采购订单");
            }
            BigDecimal inboundQty = value(orderItem.getInboundQty()).add(entry.getValue());
            if (inboundQty.signum() < 0 || inboundQty.compareTo(orderItem.getQty()) > 0) {
                throw new BizException("入库数量超过采购订单待入库数量");
            }
            orderItem.setInboundQty(inboundQty);
            orderItem.setInboundStatus(inboundStatus(inboundQty, orderItem.getQty()));
            orderItem.setModifyId(userId);
            purchaseOrderItemRepository.update(orderItem);
        }
        purchaseOrder.setInboundStatus(orderInboundStatus(orderItems));
        purchaseOrder.setModifyId(userId);
        purchaseOrder.setUpdateTime(now);
        purchaseOrderRepository.update(purchaseOrder);
    }

    private static Map<Long, BigDecimal> inboundQtyChanges(List<PurchaseInboundItem> existingItems,
                                                            List<PurchaseInboundItemDTO> submittedItems) {
        Map<Long, BigDecimal> changes = new HashMap<>();
        for (PurchaseInboundItem existingItem : existingItems) {
            changes.merge(existingItem.getPurchaseOrderItemId(), existingItem.getQty().negate(), BigDecimal::add);
        }
        for (PurchaseInboundItemDTO submittedItem : submittedItems) {
            changes.merge(submittedItem.getPurchaseOrderItemId(), submittedItem.getQty(), BigDecimal::add);
        }
        changes.entrySet().removeIf(entry -> entry.getValue().signum() == 0);
        return changes;
    }

    private static Integer orderInboundStatus(List<PurchaseOrderItem> orderItems) {
        boolean hasInbound = false;
        boolean allFullyInbounded = !orderItems.isEmpty();
        for (PurchaseOrderItem orderItem : orderItems) {
            Integer inboundStatus = inboundStatus(value(orderItem.getInboundQty()), orderItem.getQty());
            if (!InboundStatusEnum.NOT_INBOUNDED.getCode().equals(inboundStatus)) {
                hasInbound = true;
            }
            if (!InboundStatusEnum.FULLY_INBOUNDED.getCode().equals(inboundStatus)) {
                allFullyInbounded = false;
            }
        }
        if (allFullyInbounded) {
            return InboundStatusEnum.FULLY_INBOUNDED.getCode();
        }
        return hasInbound ? InboundStatusEnum.PARTIALLY_INBOUNDED.getCode()
            : InboundStatusEnum.NOT_INBOUNDED.getCode();
    }

    private static Integer inboundStatus(BigDecimal inboundQty, BigDecimal orderedQty) {
        if (inboundQty.signum() <= 0) {
            return InboundStatusEnum.NOT_INBOUNDED.getCode();
        }
        if (inboundQty.compareTo(orderedQty) >= 0) {
            return InboundStatusEnum.FULLY_INBOUNDED.getCode();
        }
        return InboundStatusEnum.PARTIALLY_INBOUNDED.getCode();
    }

    private static BigDecimal value(BigDecimal value) {
        return value == null ? BigDecimal.ZERO : value;
    }

    private static LocalDateTime occurredAt(Long inboundDate) {
        if (inboundDate == null) {
            return LocalDateTime.now();
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(inboundDate), ZoneId.systemDefault());
    }
}
