package xbb.ai.erp.module.purchase.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseOrderItemAdminAssembler;
import xbb.ai.erp.module.purchase.application.service.PurchaseOrderItemAdminAppService;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderItemRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseOrderItemAdminAppServiceImpl implements PurchaseOrderItemAdminAppService {

    private final PurchaseOrderItemRepository purchaseOrderItemRepository;

    @Override
    public ListBaseVO<PurchaseOrderItemListItemVO> list(PurchaseOrderItemListDTO dto) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", dto.getId());
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("orderId", dto.getOrderId());
        conditionMap.put("lineNo", dto.getLineNo());
        conditionMap.put("skuId", dto.getSkuId());
        conditionMap.put("skuCodeSnapshot", dto.getSkuCodeSnapshot());
        conditionMap.put("skuNameSnapshot", dto.getSkuNameSnapshot());
        conditionMap.put("purchaseUnitId", dto.getPurchaseUnitId());
        conditionMap.put("warehouseId", dto.getWarehouseId());
        conditionMap.put("isGift", dto.getIsGift());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("groupByStr", dto.getGroupByStr());
        conditionMap.put("orderByStr", dto.getOrderByStr());
        List<PurchaseOrderItem> list = purchaseOrderItemRepository.findByCondition(conditionMap);
        Long total = purchaseOrderItemRepository.count(conditionMap);
        ListBaseVO<PurchaseOrderItemListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchaseOrderItemAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseOrderItemSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<PurchaseOrderItemSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(PurchaseOrderItemAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseOrderItemSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<PurchaseOrderItemSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(PurchaseOrderItemSaveDTO dto) {
        PurchaseOrderItem purchaseOrderItem = PurchaseOrderItemAdminAssembler.toPurchaseOrderItem(dto);
        if (purchaseOrderItem.getId() == null) {
            applyInsertDefaults(purchaseOrderItem, dto.getUserId());
            purchaseOrderItemRepository.insert(purchaseOrderItem);
        } else {
            purchaseOrderItemRepository.update(purchaseOrderItem);
        }
        return purchaseOrderItem.getId();
    }

    private void applyInsertDefaults(PurchaseOrderItem purchaseOrderItem, String userId) {
        long now = System.currentTimeMillis();
        if (purchaseOrderItem.getVersion() == null) {
            purchaseOrderItem.setVersion(0);
        }
        if (purchaseOrderItem.getDeleted() == null) {
            purchaseOrderItem.setDeleted(0);
        }
        if (purchaseOrderItem.getAddTime() == null) {
            purchaseOrderItem.setAddTime(now);
        }
        if (purchaseOrderItem.getUpdateTime() == null) {
            purchaseOrderItem.setUpdateTime(now);
        }
        if (purchaseOrderItem.getCreatorId() == null || purchaseOrderItem.getCreatorId().isBlank()) {
            purchaseOrderItem.setCreatorId(userId);
        }
        if (purchaseOrderItem.getModifyId() == null || purchaseOrderItem.getModifyId().isBlank()) {
            purchaseOrderItem.setModifyId(userId);
        }
    }

    @Override
    public PurchaseOrderItemDetailVO detail(IdBaseDTO dto) {
        return PurchaseOrderItemAdminAssembler.toDetailVO(toSaveItem(dto));
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            return;
        }
        purchaseOrderItemRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchaseOrderItemSaveItemVO toSaveItem(IdBaseDTO dto) {
        return PurchaseOrderItemAdminAssembler.toSaveItemVO(purchaseOrderItemRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
