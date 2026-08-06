package xbb.ai.erp.module.purchase.application.service.query;

import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.support.AdminParamValidator;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundListDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseInboundAdminAssembler;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PurchaseInboundQueryAppServiceImpl {

    private final PurchaseInboundRepository purchaseInboundRepository;

    public PurchaseInboundQueryAppServiceImpl(PurchaseInboundRepository purchaseInboundRepository) {
        this.purchaseInboundRepository = purchaseInboundRepository;
    }

    public ListBaseVO<PurchaseInboundListItemVO> list(PurchaseInboundListDTO dto) {
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("id", dto.getId());
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("purchaseOrgId", dto.getPurchaseOrgId());
        conditionMap.put("inboundNo", dto.getInboundNo());
        conditionMap.put("sourceDocType", dto.getSourceDocType());
        conditionMap.put("sourceDocId", dto.getSourceDocId());
        conditionMap.put("vendorId", dto.getVendorId());
        conditionMap.put("warehouseId", dto.getWarehouseId());
        conditionMap.put("actualInboundTime", dto.getActualInboundTime());
        conditionMap.put("bizStatus", dto.getBizStatus());
        conditionMap.put("approvalStatus", dto.getApprovalStatus());
        conditionMap.put("executionStatus", dto.getExecutionStatus());
        conditionMap.put("inventoryFlowNo", dto.getInventoryFlowNo());
        conditionMap.put("payableTriggerStatus", dto.getPayableTriggerStatus());
        conditionMap.put("payableNo", dto.getPayableNo());
        conditionMap.put("invoiceSourceStatus", dto.getInvoiceSourceStatus());
        conditionMap.put("periodLockedFlag", dto.getPeriodLockedFlag());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("offset", dto.getOffset());
        List<PurchaseInbound> list = purchaseInboundRepository.findByCondition(conditionMap);
        Long total = purchaseInboundRepository.count(conditionMap);
        ListBaseVO<PurchaseInboundListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchaseInboundAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    public SaveItemVO<PurchaseInboundSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<PurchaseInboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(PurchaseInboundAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    public SaveItemVO<PurchaseInboundSaveItemVO> updateItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInbound entity = purchaseInboundRepository.findById(dto.getCorpid(), dto.getId());
        SaveItemVO<PurchaseInboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(PurchaseInboundAdminAssembler.toSaveItemVO(entity));
        return vo;
    }

    public PurchaseInboundDetailVO detail(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        PurchaseInbound entity = purchaseInboundRepository.findById(dto.getCorpid(), dto.getId());
        return PurchaseInboundAdminAssembler.toDetailVO(PurchaseInboundAdminAssembler.toSaveItemVO(entity));
    }
}
