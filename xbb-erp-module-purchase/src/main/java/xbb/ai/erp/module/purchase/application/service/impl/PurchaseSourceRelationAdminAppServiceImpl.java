package xbb.ai.erp.module.purchase.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseSourceRelationAdminAssembler;
import xbb.ai.erp.module.purchase.application.service.PurchaseSourceRelationAdminAppService;
import xbb.ai.erp.module.purchase.domain.model.PurchaseSourceRelation;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseSourceRelationRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseSourceRelationAdminAppServiceImpl implements PurchaseSourceRelationAdminAppService {

    private final PurchaseSourceRelationRepository purchaseSourceRelationRepository;

    @Override
    public ListBaseVO<PurchaseSourceRelationListItemVO> list(PurchaseSourceRelationListDTO dto) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", dto.getId());
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("sourceDocType", dto.getSourceDocType());
        conditionMap.put("sourceDocId", dto.getSourceDocId());
        conditionMap.put("sourceLineId", dto.getSourceLineId());
        conditionMap.put("targetDocType", dto.getTargetDocType());
        conditionMap.put("targetDocId", dto.getTargetDocId());
        conditionMap.put("targetLineId", dto.getTargetLineId());
        conditionMap.put("relationStatus", dto.getRelationStatus());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("groupByStr", dto.getGroupByStr());
        conditionMap.put("orderByStr", dto.getOrderByStr());
        List<PurchaseSourceRelation> list = purchaseSourceRelationRepository.findByCondition(conditionMap);
        Long total = purchaseSourceRelationRepository.count(conditionMap);
        ListBaseVO<PurchaseSourceRelationListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchaseSourceRelationAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseSourceRelationSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<PurchaseSourceRelationSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(PurchaseSourceRelationAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseSourceRelationSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<PurchaseSourceRelationSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(PurchaseSourceRelationSaveDTO dto) {
        PurchaseSourceRelation purchaseSourceRelation = PurchaseSourceRelationAdminAssembler.toPurchaseSourceRelation(dto);
        if (purchaseSourceRelation.getId() == null) {
            applyInsertDefaults(purchaseSourceRelation, dto.getUserId());
            purchaseSourceRelationRepository.insert(purchaseSourceRelation);
        } else {
            purchaseSourceRelationRepository.update(purchaseSourceRelation);
        }
        return purchaseSourceRelation.getId();
    }

    private void applyInsertDefaults(PurchaseSourceRelation purchaseSourceRelation, String userId) {
        long now = System.currentTimeMillis();
        if (purchaseSourceRelation.getVersion() == null) {
            purchaseSourceRelation.setVersion(0);
        }
        if (purchaseSourceRelation.getDeleted() == null) {
            purchaseSourceRelation.setDeleted(0);
        }
        if (purchaseSourceRelation.getAddTime() == null) {
            purchaseSourceRelation.setAddTime(now);
        }
        if (purchaseSourceRelation.getUpdateTime() == null) {
            purchaseSourceRelation.setUpdateTime(now);
        }
        if (purchaseSourceRelation.getCreatorId() == null || purchaseSourceRelation.getCreatorId().isBlank()) {
            purchaseSourceRelation.setCreatorId(userId);
        }
        if (purchaseSourceRelation.getModifyId() == null || purchaseSourceRelation.getModifyId().isBlank()) {
            purchaseSourceRelation.setModifyId(userId);
        }
    }

    @Override
    public PurchaseSourceRelationDetailVO detail(IdBaseDTO dto) {
        return PurchaseSourceRelationAdminAssembler.toDetailVO(toSaveItem(dto));
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            return;
        }
        purchaseSourceRelationRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchaseSourceRelationSaveItemVO toSaveItem(IdBaseDTO dto) {
        return PurchaseSourceRelationAdminAssembler.toSaveItemVO(purchaseSourceRelationRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
