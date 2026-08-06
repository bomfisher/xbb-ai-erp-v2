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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseOrderItemAdminAssembler;
import xbb.ai.erp.module.purchase.application.service.PurchaseOrderItemAdminAppService;
import xbb.ai.erp.module.purchase.application.support.PurchaseInsertDefaults;
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
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(conditionMap, "id", dto.getId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "orderId", dto.getOrderId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "lineNo", dto.getLineNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "skuId", dto.getSkuId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "skuCodeSnapshot", dto.getSkuCodeSnapshot());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "skuNameSnapshot", dto.getSkuNameSnapshot());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "purchaseUnitId", dto.getPurchaseUnitId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "warehouseId", dto.getWarehouseId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "isGift", dto.getIsGift());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageNum", dto.getPageNum());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "offset", dto.getOffset());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageSize", dto.getPageSize());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "groupByStr", dto.getGroupByStr());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "orderByStr", dto.getOrderByStr());
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
        PurchaseInsertDefaults.apply(purchaseOrderItem, userId, System.currentTimeMillis(), 1);
    }

    @Override
    public PurchaseOrderItemDetailVO detail(IdBaseDTO dto) {
        return PurchaseOrderItemAdminAssembler.toDetailVO(toSaveItem(dto));
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        AdminParamValidator.validateBatchDelete(dto);
        purchaseOrderItemRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchaseOrderItemSaveItemVO toSaveItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        return PurchaseOrderItemAdminAssembler.toSaveItemVO(purchaseOrderItemRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
