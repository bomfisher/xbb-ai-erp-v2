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
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseRequestAdminAssembler;
import xbb.ai.erp.module.purchase.application.service.PurchaseRequestAdminAppService;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseRequestRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseRequestAdminAppServiceImpl implements PurchaseRequestAdminAppService {

    private final PurchaseRequestRepository purchaseRequestRepository;

    @Override
    public ListBaseVO<PurchaseRequestListItemVO> list(PurchaseRequestListDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(conditionMap, "id", dto.getId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "purchaseOrgId", dto.getPurchaseOrgId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "requestNo", dto.getRequestNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "requestDeptId", dto.getRequestDeptId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "applicantId", dto.getApplicantId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceType", dto.getSourceType());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceNo", dto.getSourceNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "suggestedVendorId", dto.getSuggestedVendorId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "suggestedDeliveryDate", dto.getSuggestedDeliveryDate());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "bizStatus", dto.getBizStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "approvalStatus", dto.getApprovalStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageNum", dto.getPageNum());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "offset", dto.getOffset());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageSize", dto.getPageSize());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "groupByStr", dto.getGroupByStr());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "orderByStr", dto.getOrderByStr());
        List<PurchaseRequest> list = purchaseRequestRepository.findByCondition(conditionMap);
        Long total = purchaseRequestRepository.count(conditionMap);
        ListBaseVO<PurchaseRequestListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchaseRequestAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseRequestSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<PurchaseRequestSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(PurchaseRequestAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseRequestSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<PurchaseRequestSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(PurchaseRequestSaveDTO dto) {
        PurchaseRequest purchaseRequest = PurchaseRequestAdminAssembler.toPurchaseRequest(dto);
        if (purchaseRequest.getId() == null) {
            applyInsertDefaults(purchaseRequest, dto.getUserId());
            purchaseRequestRepository.insert(purchaseRequest);
        } else {
            purchaseRequestRepository.update(purchaseRequest);
        }
        return purchaseRequest.getId();
    }

    private void applyInsertDefaults(PurchaseRequest purchaseRequest, String userId) {
        long now = System.currentTimeMillis();
        if (purchaseRequest.getBizStatus() == null || purchaseRequest.getBizStatus().isBlank()) {
            purchaseRequest.setBizStatus("1");
        }
        if (purchaseRequest.getVersion() == null) {
            purchaseRequest.setVersion(0);
        }
        if (purchaseRequest.getDeleted() == null) {
            purchaseRequest.setDeleted(0);
        }
        if (purchaseRequest.getAddTime() == null) {
            purchaseRequest.setAddTime(now);
        }
        if (purchaseRequest.getUpdateTime() == null) {
            purchaseRequest.setUpdateTime(now);
        }
        if (purchaseRequest.getCreatorId() == null || purchaseRequest.getCreatorId().isBlank()) {
            purchaseRequest.setCreatorId(userId);
        }
        if (purchaseRequest.getModifyId() == null || purchaseRequest.getModifyId().isBlank()) {
            purchaseRequest.setModifyId(userId);
        }
    }

    @Override
    public PurchaseRequestDetailVO detail(IdBaseDTO dto) {
        return PurchaseRequestAdminAssembler.toDetailVO(toSaveItem(dto));
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        AdminParamValidator.validateBatchDelete(dto);
        purchaseRequestRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchaseRequestSaveItemVO toSaveItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        return PurchaseRequestAdminAssembler.toSaveItemVO(purchaseRequestRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
