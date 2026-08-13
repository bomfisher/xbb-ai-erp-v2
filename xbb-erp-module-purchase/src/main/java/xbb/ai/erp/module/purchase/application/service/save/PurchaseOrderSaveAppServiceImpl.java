package xbb.ai.erp.module.purchase.application.service.save;

import lombok.RequiredArgsConstructor;
import java.math.BigDecimal;
import java.math.RoundingMode;
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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSubmitSaveDTO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseOrderAdminAssembler;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderValidator;
import xbb.ai.erp.module.purchase.application.port.PurchaseOrderDraftRepository;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveProtocolValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveCommonValidator;
import xbb.ai.erp.module.purchase.application.validator.PurchaseOrderSaveBusinessValidator;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;

@Service
@RequiredArgsConstructor
public class PurchaseOrderSaveAppServiceImpl {

    private final PurchaseOrderRepository purchaseOrderRepository;
    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    private final PurchaseOrderDraftRepository draftRepository;
    private final PurchaseOrderSaveProtocolValidator protocolValidator;
    private final PurchaseOrderSaveCommonValidator commonValidator;
    private final PurchaseOrderSaveBusinessValidator businessValidator;

    @Transactional
    public BaseVO saveAndSubmit(PurchaseOrderSubmitSaveDTO dto) {
        protocolValidator.validate(dto);
        commonValidator.validateForSubmit(dto);
        businessValidator.validateForSubmit(dto);
        dto.getMain().setTotalAmount(totalAmount(dto.getItems()));
        Long purchaseOrderId = save(dto);
        syncItems(dto, purchaseOrderId);
        if (dto.getDraftMeta() != null && dto.getDraftMeta().getDraftCode() != null) draftRepository.removeDraft(dto.getCorpid(), dto.getDraftMeta().getDraftCode());
        return new BaseVO();
    }

    public Long save(PurchaseOrderSaveDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        PurchaseOrderValidator.validateSave(dto);
        PurchaseOrder entity = PurchaseOrderAdminAssembler.toPurchaseOrder(dto);
        long now = System.currentTimeMillis();
        entity.setModifyId(dto.getUserId());
        entity.setUpdateTime(now);
        if (entity.getId() == null) {
            entity.setCreatorId(dto.getUserId());
            entity.setAddTime(now);
            entity.setDel(0);
            if (entity.getSupplierName() == null || entity.getSupplierName().isBlank()) entity.setSupplierName("MOCK");
            if (entity.getTotalAmount() == null) entity.setTotalAmount(BigDecimal.ZERO);
            if (entity.getStatus() == null || entity.getStatus().isBlank()) entity.setStatus("1");
            return purchaseOrderRepository.insert(entity);
        }
        purchaseOrderRepository.update(entity);
        return entity.getId();
    }

    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() != null && !dto.getIdList().isEmpty()) {
            purchaseOrderRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
        }
    }

    private void syncItems(PurchaseOrderSaveDTO dto, Long purchaseOrderId) {
        List<PurchaseOrderItem> existingItems = purchaseOrderItemRepository.findByCondition(
            Map.of("corpid", dto.getCorpid(), "purchaseOrderId", purchaseOrderId));
        Map<Long, PurchaseOrderItem> existingById = new HashMap<>();
        existingItems.forEach(item -> existingById.put(item.getId(), item));

        Set<Long> submittedIds = new HashSet<>();
        List<PurchaseOrderItem> itemsToInsert = new ArrayList<>();
        List<PurchaseOrderItem> itemsToUpdate = new ArrayList<>();
        for (int index = 0; index < dto.getItems().size(); index++) {
            PurchaseOrderItem item = PurchaseOrderAdminAssembler.toPurchaseOrderItem(dto.getItems().get(index), dto.getCorpid(),
                purchaseOrderId, index + 1, dto.getUserId());
            if (item.getId() == null) {
                itemsToInsert.add(item);
                continue;
            }
            if (!submittedIds.add(item.getId())) {
                throw new xbb.ai.erp.base.common.exception.BizException("采购产品明细ID重复");
            }
            PurchaseOrderItem existing = existingById.get(item.getId());
            if (existing == null) {
                throw new xbb.ai.erp.base.common.exception.BizException("采购产品明细不存在或不属于当前订单");
            }
            item.setInboundQty(existing.getInboundQty());
            item.setCreatorId(existing.getCreatorId());
            itemsToUpdate.add(item);
        }
        List<Long> idsToRemove = existingById.keySet().stream().filter(id -> !submittedIds.contains(id)).toList();
        boolean hasInboundItemsToRemove = idsToRemove.stream()
            .map(existingById::get)
            .anyMatch(item -> item.getInboundQty() != null && item.getInboundQty().compareTo(BigDecimal.ZERO) > 0);
        if (hasInboundItemsToRemove) {
            throw new xbb.ai.erp.base.common.exception.BizException("已有入库数量的采购产品不能删除");
        }
        if (!idsToRemove.isEmpty()) {
            purchaseOrderItemRepository.removeBatchByIds(dto.getCorpid(), idsToRemove);
        }
        itemsToUpdate.forEach(purchaseOrderItemRepository::update);
        if (!itemsToInsert.isEmpty()) {
            purchaseOrderItemRepository.insertBatch(itemsToInsert);
        }
    }

    private static BigDecimal totalAmount(List<PurchaseOrderItemDTO> items) {
        return items.stream().map(item -> item.getQty().multiply(item.getUnitPrice()))
            .reduce(BigDecimal.ZERO, BigDecimal::add).setScale(2, RoundingMode.HALF_UP);
    }
}
