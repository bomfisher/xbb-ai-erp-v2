package xbb.ai.erp.module.purchase.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
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
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", dto.getId());
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("purchaseOrgId", dto.getPurchaseOrgId());
        conditionMap.put("requestNo", dto.getRequestNo());
        conditionMap.put("requestDeptId", dto.getRequestDeptId());
        conditionMap.put("applicantId", dto.getApplicantId());
        conditionMap.put("sourceType", dto.getSourceType());
        conditionMap.put("sourceNo", dto.getSourceNo());
        conditionMap.put("suggestedVendorId", dto.getSuggestedVendorId());
        conditionMap.put("suggestedDeliveryDate", dto.getSuggestedDeliveryDate());
        conditionMap.put("bizStatus", dto.getBizStatus());
        conditionMap.put("approvalStatus", dto.getApprovalStatus());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("groupByStr", dto.getGroupByStr());
        conditionMap.put("orderByStr", dto.getOrderByStr());
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
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            return;
        }
        purchaseRequestRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchaseRequestSaveItemVO toSaveItem(IdBaseDTO dto) {
        return PurchaseRequestAdminAssembler.toSaveItemVO(purchaseRequestRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
