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
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(conditionMap, "id", dto.getId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "purchaseOrgId", dto.getPurchaseOrgId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "taskNo", dto.getTaskNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceType", dto.getSourceType());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceDocId", dto.getSourceDocId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceLineId", dto.getSourceLineId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceDocNo", dto.getSourceDocNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "skuId", dto.getSkuId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "skuCodeSnapshot", dto.getSkuCodeSnapshot());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "skuNameSnapshot", dto.getSkuNameSnapshot());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "suggestedVendorId", dto.getSuggestedVendorId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "suggestedDeliveryDate", dto.getSuggestedDeliveryDate());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "priorityLevel", dto.getPriorityLevel());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "taskStatus", dto.getTaskStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "salesLinkedFlag", dto.getSalesLinkedFlag());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageNum", dto.getPageNum());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "offset", dto.getOffset());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageSize", dto.getPageSize());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "groupByStr", dto.getGroupByStr());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "orderByStr", dto.getOrderByStr());
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
        AdminParamValidator.validateBatchDelete(dto);
        purchasePendingTaskRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchasePendingTaskSaveItemVO toSaveItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        return PurchasePendingTaskAdminAssembler.toSaveItemVO(purchasePendingTaskRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
