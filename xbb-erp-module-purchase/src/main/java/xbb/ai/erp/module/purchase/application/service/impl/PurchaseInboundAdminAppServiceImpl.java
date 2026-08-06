package xbb.ai.erp.module.purchase.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseInboundAdminAssembler;
import xbb.ai.erp.module.purchase.application.service.PurchaseInboundAdminAppService;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseInboundRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseInboundAdminAppServiceImpl implements PurchaseInboundAdminAppService {

    private final PurchaseInboundRepository purchaseInboundRepository;

    @Override
    public ListBaseVO<PurchaseInboundListItemVO> list(PurchaseInboundListDTO dto) {
        Map<String, Object> conditionMap = new HashMap<>();
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
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("groupByStr", dto.getGroupByStr());
        conditionMap.put("orderByStr", dto.getOrderByStr());
        List<PurchaseInbound> list = purchaseInboundRepository.findByCondition(conditionMap);
        Long total = purchaseInboundRepository.count(conditionMap);
        ListBaseVO<PurchaseInboundListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchaseInboundAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseInboundSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<PurchaseInboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(PurchaseInboundAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseInboundSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<PurchaseInboundSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(PurchaseInboundSaveDTO dto) {
        PurchaseInbound purchaseInbound = PurchaseInboundAdminAssembler.toPurchaseInbound(dto);
        if (purchaseInbound.getId() == null) {
            purchaseInboundRepository.insert(purchaseInbound);
        } else {
            purchaseInboundRepository.update(purchaseInbound);
        }
        return purchaseInbound.getId();
    }

    @Override
    public PurchaseInboundDetailVO detail(IdBaseDTO dto) {
        return PurchaseInboundAdminAssembler.toDetailVO(toSaveItem(dto));
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            return;
        }
        purchaseInboundRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchaseInboundSaveItemVO toSaveItem(IdBaseDTO dto) {
        return PurchaseInboundAdminAssembler.toSaveItemVO(purchaseInboundRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
