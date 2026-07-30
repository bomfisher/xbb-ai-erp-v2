package xbb.ai.erp.module.purchase.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.support.QueryConditionMapHelper;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseRequestItemAdminAssembler;
import xbb.ai.erp.module.purchase.application.service.PurchaseRequestItemAdminAppService;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestItemRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseRequestItemAdminAppServiceImpl implements PurchaseRequestItemAdminAppService {

    private final PurchaseRequestItemRepository purchaseRequestItemRepository;

    @Override
    public ListBaseVO<PurchaseRequestItemListItemVO> list(PurchaseRequestItemListDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(conditionMap, "id", dto.getId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "requestId", dto.getRequestId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "lineNo", dto.getLineNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "skuId", dto.getSkuId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "skuCodeSnapshot", dto.getSkuCodeSnapshot());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "skuNameSnapshot", dto.getSkuNameSnapshot());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "purchaseUnitId", dto.getPurchaseUnitId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "suggestedVendorId", dto.getSuggestedVendorId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "suggestedDeliveryDate", dto.getSuggestedDeliveryDate());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageNum", dto.getPageNum());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "offset", dto.getOffset());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageSize", dto.getPageSize());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "groupByStr", dto.getGroupByStr());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "orderByStr", dto.getOrderByStr());
        List<PurchaseRequestItem> list = purchaseRequestItemRepository.findByCondition(conditionMap);
        Long total = purchaseRequestItemRepository.count(conditionMap);
        ListBaseVO<PurchaseRequestItemListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchaseRequestItemAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseRequestItemSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<PurchaseRequestItemSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(PurchaseRequestItemAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseRequestItemSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<PurchaseRequestItemSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(PurchaseRequestItemSaveDTO dto) {
        PurchaseRequestItem purchaseRequestItem = PurchaseRequestItemAdminAssembler.toPurchaseRequestItem(dto);
        if (purchaseRequestItem.getId() == null) {
            applyInsertDefaults(purchaseRequestItem, dto.getUserId());
            purchaseRequestItemRepository.insert(purchaseRequestItem);
        } else {
            purchaseRequestItemRepository.update(purchaseRequestItem);
        }
        return purchaseRequestItem.getId();
    }

    private void applyInsertDefaults(PurchaseRequestItem purchaseRequestItem, String userId) {
        long now = System.currentTimeMillis();
        if (purchaseRequestItem.getVersion() == null) {
            purchaseRequestItem.setVersion(0);
        }
        if (purchaseRequestItem.getDeleted() == null) {
            purchaseRequestItem.setDeleted(0);
        }
        if (purchaseRequestItem.getAddTime() == null) {
            purchaseRequestItem.setAddTime(now);
        }
        if (purchaseRequestItem.getUpdateTime() == null) {
            purchaseRequestItem.setUpdateTime(now);
        }
        if (purchaseRequestItem.getCreatorId() == null || purchaseRequestItem.getCreatorId().isBlank()) {
            purchaseRequestItem.setCreatorId(userId);
        }
        if (purchaseRequestItem.getModifyId() == null || purchaseRequestItem.getModifyId().isBlank()) {
            purchaseRequestItem.setModifyId(userId);
        }
    }

    @Override
    public PurchaseRequestItemDetailVO detail(IdBaseDTO dto) {
        return PurchaseRequestItemAdminAssembler.toDetailVO(toSaveItem(dto));
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        AdminParamValidator.validateBatchDelete(dto);
        purchaseRequestItemRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchaseRequestItemSaveItemVO toSaveItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        return PurchaseRequestItemAdminAssembler.toSaveItemVO(purchaseRequestItemRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
