package xbb.ai.erp.module.purchase.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
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
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", dto.getId());
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("requestId", dto.getRequestId());
        conditionMap.put("lineNo", dto.getLineNo());
        conditionMap.put("skuId", dto.getSkuId());
        conditionMap.put("skuCodeSnapshot", dto.getSkuCodeSnapshot());
        conditionMap.put("skuNameSnapshot", dto.getSkuNameSnapshot());
        conditionMap.put("purchaseUnitId", dto.getPurchaseUnitId());
        conditionMap.put("suggestedVendorId", dto.getSuggestedVendorId());
        conditionMap.put("suggestedDeliveryDate", dto.getSuggestedDeliveryDate());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("groupByStr", dto.getGroupByStr());
        conditionMap.put("orderByStr", dto.getOrderByStr());
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
            purchaseRequestItemRepository.insert(purchaseRequestItem);
        } else {
            purchaseRequestItemRepository.update(purchaseRequestItem);
        }
        return purchaseRequestItem.getId();
    }

    @Override
    public PurchaseRequestItemDetailVO detail(IdBaseDTO dto) {
        return PurchaseRequestItemAdminAssembler.toDetailVO(toSaveItem(dto));
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            return;
        }
        purchaseRequestItemRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchaseRequestItemSaveItemVO toSaveItem(IdBaseDTO dto) {
        return PurchaseRequestItemAdminAssembler.toSaveItemVO(purchaseRequestItemRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
