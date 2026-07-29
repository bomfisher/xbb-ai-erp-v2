package xbb.ai.erp.module.purchase.application.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import xbb.ai.erp.base.common.dto.BaseDTO;
import xbb.ai.erp.base.common.dto.BatchBaseDTO;
import xbb.ai.erp.base.common.dto.IdBaseDTO;
import xbb.ai.erp.base.common.vo.ListBaseVO;
import xbb.ai.erp.base.common.vo.SaveItemVO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderListDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;
import xbb.ai.erp.module.purchase.application.assembler.PurchaseOrderAdminAssembler;
import xbb.ai.erp.module.purchase.application.service.PurchaseOrderAdminAppService;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.repository.PurchaseOrderRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class PurchaseOrderAdminAppServiceImpl implements PurchaseOrderAdminAppService {

    private final PurchaseOrderRepository purchaseOrderRepository;

    @Override
    public ListBaseVO<PurchaseOrderListItemVO> list(PurchaseOrderListDTO dto) {
        Map<String, Object> conditionMap = new HashMap<>();
        conditionMap.put("id", dto.getId());
        conditionMap.put("corpid", dto.getCorpid());
        conditionMap.put("purchaseOrgId", dto.getPurchaseOrgId());
        conditionMap.put("orderNo", dto.getOrderNo());
        conditionMap.put("vendorId", dto.getVendorId());
        conditionMap.put("vendorNameSnapshot", dto.getVendorNameSnapshot());
        conditionMap.put("purchaserId", dto.getPurchaserId());
        conditionMap.put("warehouseId", dto.getWarehouseId());
        conditionMap.put("settlementMethodId", dto.getSettlementMethodId());
        conditionMap.put("currencyCode", dto.getCurrencyCode());
        conditionMap.put("deliveryDate", dto.getDeliveryDate());
        conditionMap.put("sourceType", dto.getSourceType());
        conditionMap.put("sourceNo", dto.getSourceNo());
        conditionMap.put("salesLinkedFlag", dto.getSalesLinkedFlag());
        conditionMap.put("bizStatus", dto.getBizStatus());
        conditionMap.put("approvalStatus", dto.getApprovalStatus());
        conditionMap.put("executionStatus", dto.getExecutionStatus());
        conditionMap.put("receiptStatus", dto.getReceiptStatus());
        conditionMap.put("inboundStatus", dto.getInboundStatus());
        conditionMap.put("payableStatus", dto.getPayableStatus());
        conditionMap.put("invoiceStatus", dto.getInvoiceStatus());
        conditionMap.put("paymentStatus", dto.getPaymentStatus());
        conditionMap.put("periodLockedFlag", dto.getPeriodLockedFlag());
        conditionMap.put("pageNum", dto.getPageNum());
        conditionMap.put("offset", dto.getOffset());
        conditionMap.put("pageSize", dto.getPageSize());
        conditionMap.put("groupByStr", dto.getGroupByStr());
        conditionMap.put("orderByStr", dto.getOrderByStr());
        List<PurchaseOrder> list = purchaseOrderRepository.findByCondition(conditionMap);
        Long total = purchaseOrderRepository.count(conditionMap);
        ListBaseVO<PurchaseOrderListItemVO> vo = new ListBaseVO<>();
        vo.setList(list.stream().map(PurchaseOrderAdminAssembler::toListItemVO).toList());
        vo.setPageHelper(new ListBaseVO.PageHelper(dto.getPageNum() == null ? 1 : dto.getPageNum(), total == null ? 0 : total.intValue()));
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseOrderSaveItemVO> addItem(BaseDTO dto) {
        SaveItemVO<PurchaseOrderSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(PurchaseOrderAdminAssembler.buildEmptySaveItemVO());
        return vo;
    }

    @Override
    public SaveItemVO<PurchaseOrderSaveItemVO> updateItem(IdBaseDTO dto) {
        SaveItemVO<PurchaseOrderSaveItemVO> vo = new SaveItemVO<>();
        vo.setData(toSaveItem(dto));
        return vo;
    }

    @Override
    public Long save(PurchaseOrderSaveDTO dto) {
        PurchaseOrder purchaseOrder = PurchaseOrderAdminAssembler.toPurchaseOrder(dto);
        if (purchaseOrder.getId() == null) {
            applyInsertDefaults(purchaseOrder, dto.getUserId());
            purchaseOrderRepository.insert(purchaseOrder);
        } else {
            purchaseOrderRepository.update(purchaseOrder);
        }
        return purchaseOrder.getId();
    }

    private void applyInsertDefaults(PurchaseOrder purchaseOrder, String userId) {
        long now = System.currentTimeMillis();
        if (purchaseOrder.getBizStatus() == null || purchaseOrder.getBizStatus().isBlank()) {
            purchaseOrder.setBizStatus("1");
        }
        if (purchaseOrder.getVersion() == null) {
            purchaseOrder.setVersion(0);
        }
        if (purchaseOrder.getDeleted() == null) {
            purchaseOrder.setDeleted(0);
        }
        if (purchaseOrder.getAddTime() == null) {
            purchaseOrder.setAddTime(now);
        }
        if (purchaseOrder.getUpdateTime() == null) {
            purchaseOrder.setUpdateTime(now);
        }
        if (purchaseOrder.getCreatorId() == null || purchaseOrder.getCreatorId().isBlank()) {
            purchaseOrder.setCreatorId(userId);
        }
        if (purchaseOrder.getModifyId() == null || purchaseOrder.getModifyId().isBlank()) {
            purchaseOrder.setModifyId(userId);
        }
    }

    @Override
    public PurchaseOrderDetailVO detail(IdBaseDTO dto) {
        return PurchaseOrderAdminAssembler.toDetailVO(toSaveItem(dto));
    }

    @Override
    public void delete(BatchBaseDTO dto) {
        if (dto.getIdList() == null || dto.getIdList().isEmpty()) {
            return;
        }
        purchaseOrderRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchaseOrderSaveItemVO toSaveItem(IdBaseDTO dto) {
        return PurchaseOrderAdminAssembler.toSaveItemVO(purchaseOrderRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
