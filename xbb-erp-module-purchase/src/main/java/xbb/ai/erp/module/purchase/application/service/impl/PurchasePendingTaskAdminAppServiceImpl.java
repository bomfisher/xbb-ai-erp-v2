package xbb.ai.erp.module.purchase.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchasePendingTaskAdminAssembler;
import xbb.ai.erp.module.purchase.application.service.PurchasePendingTaskAdminAppService;
import xbb.ai.erp.module.purchase.domain.model.PurchasePendingTask;
import xbb.ai.erp.module.purchase.domain.repository.PurchasePendingTaskRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchasePendingTaskAdminAppServiceImpl implements PurchasePendingTaskAdminAppService {

    private final PurchasePendingTaskRepository purchasePendingTaskRepository;

    @Override
    public ListBaseVO<PurchasePendingTaskListItemVO> list(PurchasePendingTaskListDTO dto) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", dto.getId());
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("purchaseOrgId", dto.getPurchaseOrgId());
        conditionMap.put("taskNo", dto.getTaskNo());
        conditionMap.put("sourceType", dto.getSourceType());
        conditionMap.put("sourceDocId", dto.getSourceDocId());
        conditionMap.put("sourceLineId", dto.getSourceLineId());
        conditionMap.put("sourceDocNo", dto.getSourceDocNo());
        conditionMap.put("skuId", dto.getSkuId());
        conditionMap.put("skuCodeSnapshot", dto.getSkuCodeSnapshot());
        conditionMap.put("skuNameSnapshot", dto.getSkuNameSnapshot());
        conditionMap.put("suggestedVendorId", dto.getSuggestedVendorId());
        conditionMap.put("suggestedDeliveryDate", dto.getSuggestedDeliveryDate());
        conditionMap.put("priorityLevel", dto.getPriorityLevel());
        conditionMap.put("taskStatus", dto.getTaskStatus());
        conditionMap.put("salesLinkedFlag", dto.getSalesLinkedFlag());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("groupByStr", dto.getGroupByStr());
        conditionMap.put("orderByStr", dto.getOrderByStr());
        List<PurchasePendingTask> list = purchasePendingTaskRepository.findByCondition(conditionMap);
        Long total = purchasePendingTaskRepository.count(conditionMap);
        ListBaseVO<PurchasePendingTaskListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchasePendingTaskAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<PurchasePendingTaskSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<PurchasePendingTaskSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(PurchasePendingTaskAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<PurchasePendingTaskSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<PurchasePendingTaskSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(PurchasePendingTaskSaveDTO dto) {
        PurchasePendingTask purchasePendingTask = PurchasePendingTaskAdminAssembler.toPurchasePendingTask(dto);
        if (purchasePendingTask.getId() == null) {
            applyInsertDefaults(purchasePendingTask, dto.getUserId());
            purchasePendingTaskRepository.insert(purchasePendingTask);
        } else {
            purchasePendingTaskRepository.update(purchasePendingTask);
        }
        return purchasePendingTask.getId();
    }

    private void applyInsertDefaults(PurchasePendingTask purchasePendingTask, String userId) {
        long now = System.currentTimeMillis();
        if (purchasePendingTask.getVersion() == null) {
            purchasePendingTask.setVersion(0);
        }
        if (purchasePendingTask.getDeleted() == null) {
            purchasePendingTask.setDeleted(0);
        }
        if (purchasePendingTask.getAddTime() == null) {
            purchasePendingTask.setAddTime(now);
        }
        if (purchasePendingTask.getUpdateTime() == null) {
            purchasePendingTask.setUpdateTime(now);
        }
        if (purchasePendingTask.getCreatorId() == null || purchasePendingTask.getCreatorId().isBlank()) {
            purchasePendingTask.setCreatorId(userId);
        }
        if (purchasePendingTask.getModifyId() == null || purchasePendingTask.getModifyId().isBlank()) {
            purchasePendingTask.setModifyId(userId);
        }
    }

    @Override
    public PurchasePendingTaskDetailVO detail(IdBaseDTO dto) {
        return PurchasePendingTaskAdminAssembler.toDetailVO(toSaveItem(dto));
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            return;
        }
        purchasePendingTaskRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchasePendingTaskSaveItemVO toSaveItem(IdBaseDTO dto) {
        return PurchasePendingTaskAdminAssembler.toSaveItemVO(purchasePendingTaskRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
