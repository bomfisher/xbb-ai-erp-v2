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
        AdminParamValidator.requireCorpid(dto);
        Map<String, Object> conditionMap = QueryConditionMapHelper.newConditionMap();
        QueryConditionMapHelper.putIfNotNull(conditionMap, "id", dto.getId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "corpid", dto.getCorpid());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "purchaseOrgId", dto.getPurchaseOrgId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "orderNo", dto.getOrderNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "vendorId", dto.getVendorId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "vendorNameSnapshot", dto.getVendorNameSnapshot());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "purchaserId", dto.getPurchaserId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "warehouseId", dto.getWarehouseId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "settlementMethodId", dto.getSettlementMethodId());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "currencyCode", dto.getCurrencyCode());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "deliveryDate", dto.getDeliveryDate());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceType", dto.getSourceType());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "sourceNo", dto.getSourceNo());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "salesLinkedFlag", dto.getSalesLinkedFlag());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "bizStatus", dto.getBizStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "approvalStatus", dto.getApprovalStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "executionStatus", dto.getExecutionStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "receiptStatus", dto.getReceiptStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "inboundStatus", dto.getInboundStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "payableStatus", dto.getPayableStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "invoiceStatus", dto.getInvoiceStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "paymentStatus", dto.getPaymentStatus());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "periodLockedFlag", dto.getPeriodLockedFlag());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageNum", dto.getPageNum());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "offset", dto.getOffset());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "pageSize", dto.getPageSize());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "groupByStr", dto.getGroupByStr());
        QueryConditionMapHelper.putIfNotNull(conditionMap, "orderByStr", dto.getOrderByStr());
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
        AdminParamValidator.validateBatchDelete(dto);
        purchaseOrderRepository.removeBatchByIds(dto.getCorpid(), dto.getIdList());
    }

    private PurchaseOrderSaveItemVO toSaveItem(IdBaseDTO dto) {
        AdminParamValidator.validateIdQuery(dto);
        return PurchaseOrderAdminAssembler.toSaveItemVO(purchaseOrderRepository.findById(dto.getCorpid(), dto.getId()));
    }
}
