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
import org.springframework.transaction.annotation.Transactional;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.BaseVO;
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

@Service
@RequiredArgsConstructor
public class PurchaseInboundSaveAppServiceImpl {

    private final PurchaseInboundRepository purchaseInboundRepository;
    private final PurchaseInboundItemRepository purchaseInboundItemRepository;

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
        Long purchaseInboundId = save(dto);
        syncItems(dto, purchaseInboundId);
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

    private void confirmInbound(Long inboundId, String corpid, String userId) {
        PurchaseInbound inbound = purchaseInboundRepository.findById(corpid, inboundId);
        if (inbound == null) {
            throw new BizException("采购入库单不存在");
        }
        if ("INVENTORY_POSTED".equals(inbound.getStatus())) {
            return;
        }
        if (!"SUBMITTED".equals(inbound.getStatus())) {
            throw new BizException("采购入库单尚未提交，不能确认入库");
        }
        List<PurchaseInboundItem> items = purchaseInboundItemRepository.findByCondition(
            Map.of("corpid", corpid, "purchaseInboundId", inboundId));
        if (items.isEmpty()) {
            throw new BizException("采购入库单没有可入账的产品明细");
        }
        List<InboundLine> lines = items.stream().map(item -> new InboundLine(
            inbound.getWarehouseId(), item.getSkuId(), item.getQty(), item.getCostAmount(), item.getId())).toList();
        inventoryCommandApi.postInbound(new InboundCommand(corpid, "PURCHASE_INBOUND", inboundId, "PURCHASE_INBOUND",
            lines, userId, occurredAt(inbound.getInboundDate()), "PURCHASE_INBOUND:" + inboundId + ":POST"));
        inbound.setStatus("INVENTORY_POSTED");
        inbound.setModifyId(userId);
        inbound.setUpdateTime(System.currentTimeMillis());
        purchaseInboundRepository.update(inbound);
    }

    private void rejectPostedInboundModification(PurchaseInboundSubmitSaveDTO dto) {
        if (dto.getMain().getId() == null) {
            return;
        }
        PurchaseInbound existing = purchaseInboundRepository.findById(dto.getCorpid(), dto.getMain().getId());
        if (existing != null && "INVENTORY_POSTED".equals(existing.getStatus())) {
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
            if (entity.getStatus() == null || entity.getStatus().isBlank()) {
                entity.setStatus("SUBMITTED");
            }
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

    private void syncItems(PurchaseInboundSaveDTO dto, Long purchaseInboundId) {
        List<PurchaseInboundItem> existingItems = purchaseInboundItemRepository.findByCondition(
            Map.of("corpid", dto.getCorpid(), "purchaseInboundId", purchaseInboundId));
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

    private static LocalDateTime occurredAt(Long inboundDate) {
        if (inboundDate == null) {
            return LocalDateTime.now();
        }
        return LocalDateTime.ofInstant(Instant.ofEpochMilli(inboundDate), ZoneId.systemDefault());
    }
}
