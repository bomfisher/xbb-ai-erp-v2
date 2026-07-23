package xbb.ai.erp.module.purchase.application.assembler;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;

public final class PurchaseOrderAdminAssembler {

    private PurchaseOrderAdminAssembler() {
    }

    public static PurchaseOrderSaveItemVO buildEmptySaveItemVO() {
        return new PurchaseOrderSaveItemVO();
    }

    public static PurchaseOrder toPurchaseOrder(PurchaseOrderSaveDTO dto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        PurchaseOrderMainDTO main = dto.getMain();
        if (main != null) {
            purchaseOrder.setId(main.getId());
            purchaseOrder.setCorpid(main.getCorpid());
            purchaseOrder.setPurchaseOrgId(main.getPurchaseOrgId());
            purchaseOrder.setOrderNo(main.getOrderNo());
            purchaseOrder.setVendorId(main.getVendorId());
            purchaseOrder.setVendorNameSnapshot(main.getVendorNameSnapshot());
            purchaseOrder.setPurchaserId(main.getPurchaserId());
            purchaseOrder.setPurchaserNameSnapshot(main.getPurchaserNameSnapshot());
            purchaseOrder.setWarehouseId(main.getWarehouseId());
            purchaseOrder.setWarehouseNameSnapshot(main.getWarehouseNameSnapshot());
            purchaseOrder.setSettlementMethodId(main.getSettlementMethodId());
            purchaseOrder.setSettlementMethodSnapshot(main.getSettlementMethodSnapshot());
            purchaseOrder.setPaymentTermSnapshot(main.getPaymentTermSnapshot());
            purchaseOrder.setCurrencyCode(main.getCurrencyCode());
            purchaseOrder.setDeliveryDate(main.getDeliveryDate());
            purchaseOrder.setSourceType(main.getSourceType());
            purchaseOrder.setSourceNo(main.getSourceNo());
            purchaseOrder.setSalesLinkedFlag(main.getSalesLinkedFlag());
            purchaseOrder.setBizStatus(main.getBizStatus());
            purchaseOrder.setApprovalStatus(main.getApprovalStatus());
            purchaseOrder.setExecutionStatus(main.getExecutionStatus());
            purchaseOrder.setReceiptStatus(main.getReceiptStatus());
            purchaseOrder.setInboundStatus(main.getInboundStatus());
            purchaseOrder.setPayableStatus(main.getPayableStatus());
            purchaseOrder.setInvoiceStatus(main.getInvoiceStatus());
            purchaseOrder.setPaymentStatus(main.getPaymentStatus());
            purchaseOrder.setGrossAmount(main.getGrossAmount());
            purchaseOrder.setNetAmount(main.getNetAmount());
            purchaseOrder.setTaxAmount(main.getTaxAmount());
            purchaseOrder.setInboundedQtySummary(main.getInboundedQtySummary());
            purchaseOrder.setUninboundedQtySummary(main.getUninboundedQtySummary());
            purchaseOrder.setClosedQtySummary(main.getClosedQtySummary());
            purchaseOrder.setPayableAmountSummary(main.getPayableAmountSummary());
            purchaseOrder.setPaidAmountSummary(main.getPaidAmountSummary());
            purchaseOrder.setInvoicedAmountSummary(main.getInvoicedAmountSummary());
            purchaseOrder.setLastInboundTime(main.getLastInboundTime());
            purchaseOrder.setLastPayableTime(main.getLastPayableTime());
            purchaseOrder.setPeriodLockedFlag(main.getPeriodLockedFlag());
            purchaseOrder.setVersion(main.getVersion());
            purchaseOrder.setRemark(main.getRemark());
            purchaseOrder.setDeleted(main.getDeleted());
            purchaseOrder.setAddTime(main.getAddTime());
            purchaseOrder.setUpdateTime(main.getUpdateTime());
            purchaseOrder.setCreatorId(main.getCreatorId());
            purchaseOrder.setModifyId(main.getModifyId());
        }
        purchaseOrder.setCorpid(dto.getCorpid());
        return purchaseOrder;
    }

    public static PurchaseOrderListItemVO toListItemVO(PurchaseOrder purchaseOrder) {
        PurchaseOrderListItemVO vo = new PurchaseOrderListItemVO();
        vo.setId(purchaseOrder.getId());
        vo.setPurchaseOrgId(purchaseOrder.getPurchaseOrgId());
        vo.setOrderNo(purchaseOrder.getOrderNo());
        vo.setVendorId(purchaseOrder.getVendorId());
        vo.setVendorNameSnapshot(purchaseOrder.getVendorNameSnapshot());
        vo.setPurchaserNameSnapshot(purchaseOrder.getPurchaserNameSnapshot());
        vo.setWarehouseNameSnapshot(purchaseOrder.getWarehouseNameSnapshot());
        vo.setCurrencyCode(purchaseOrder.getCurrencyCode());
        vo.setDeliveryDate(purchaseOrder.getDeliveryDate());
        vo.setBizStatus(purchaseOrder.getBizStatus());
        vo.setApprovalStatus(purchaseOrder.getApprovalStatus());
        vo.setExecutionStatus(purchaseOrder.getExecutionStatus());
        vo.setReceiptStatus(purchaseOrder.getReceiptStatus());
        vo.setInboundStatus(purchaseOrder.getInboundStatus());
        vo.setGrossAmount(purchaseOrder.getGrossAmount());
        vo.setNetAmount(purchaseOrder.getNetAmount());
        vo.setTaxAmount(purchaseOrder.getTaxAmount());
        vo.setInboundedQtySummary(purchaseOrder.getInboundedQtySummary());
        vo.setUninboundedQtySummary(purchaseOrder.getUninboundedQtySummary());
        vo.setClosedQtySummary(purchaseOrder.getClosedQtySummary());
        vo.setAddTime(purchaseOrder.getAddTime());
        vo.setUpdateTime(purchaseOrder.getUpdateTime());
        return vo;
    }

    public static PurchaseOrderSaveItemVO toSaveItemVO(PurchaseOrder purchaseOrder) {
        PurchaseOrderSaveItemVO vo = new PurchaseOrderSaveItemVO();
        if (purchaseOrder == null) {
            return vo;
        }
        PurchaseOrderMainDTO main = new PurchaseOrderMainDTO();
        main.setId(purchaseOrder.getId());
        main.setCorpid(purchaseOrder.getCorpid());
        main.setPurchaseOrgId(purchaseOrder.getPurchaseOrgId());
        main.setOrderNo(purchaseOrder.getOrderNo());
        main.setVendorId(purchaseOrder.getVendorId());
        main.setVendorNameSnapshot(purchaseOrder.getVendorNameSnapshot());
        main.setPurchaserId(purchaseOrder.getPurchaserId());
        main.setPurchaserNameSnapshot(purchaseOrder.getPurchaserNameSnapshot());
        main.setWarehouseId(purchaseOrder.getWarehouseId());
        main.setWarehouseNameSnapshot(purchaseOrder.getWarehouseNameSnapshot());
        main.setSettlementMethodId(purchaseOrder.getSettlementMethodId());
        main.setSettlementMethodSnapshot(purchaseOrder.getSettlementMethodSnapshot());
        main.setPaymentTermSnapshot(purchaseOrder.getPaymentTermSnapshot());
        main.setCurrencyCode(purchaseOrder.getCurrencyCode());
        main.setDeliveryDate(purchaseOrder.getDeliveryDate());
        main.setSourceType(purchaseOrder.getSourceType());
        main.setSourceNo(purchaseOrder.getSourceNo());
        main.setSalesLinkedFlag(purchaseOrder.getSalesLinkedFlag());
        main.setBizStatus(purchaseOrder.getBizStatus());
        main.setApprovalStatus(purchaseOrder.getApprovalStatus());
        main.setExecutionStatus(purchaseOrder.getExecutionStatus());
        main.setReceiptStatus(purchaseOrder.getReceiptStatus());
        main.setInboundStatus(purchaseOrder.getInboundStatus());
        main.setPayableStatus(purchaseOrder.getPayableStatus());
        main.setInvoiceStatus(purchaseOrder.getInvoiceStatus());
        main.setPaymentStatus(purchaseOrder.getPaymentStatus());
        main.setGrossAmount(purchaseOrder.getGrossAmount());
        main.setNetAmount(purchaseOrder.getNetAmount());
        main.setTaxAmount(purchaseOrder.getTaxAmount());
        main.setInboundedQtySummary(purchaseOrder.getInboundedQtySummary());
        main.setUninboundedQtySummary(purchaseOrder.getUninboundedQtySummary());
        main.setClosedQtySummary(purchaseOrder.getClosedQtySummary());
        main.setPayableAmountSummary(purchaseOrder.getPayableAmountSummary());
        main.setPaidAmountSummary(purchaseOrder.getPaidAmountSummary());
        main.setInvoicedAmountSummary(purchaseOrder.getInvoicedAmountSummary());
        main.setLastInboundTime(purchaseOrder.getLastInboundTime());
        main.setLastPayableTime(purchaseOrder.getLastPayableTime());
        main.setPeriodLockedFlag(purchaseOrder.getPeriodLockedFlag());
        main.setVersion(purchaseOrder.getVersion());
        main.setRemark(purchaseOrder.getRemark());
        main.setDeleted(purchaseOrder.getDeleted());
        main.setAddTime(purchaseOrder.getAddTime());
        main.setUpdateTime(purchaseOrder.getUpdateTime());
        main.setCreatorId(purchaseOrder.getCreatorId());
        main.setModifyId(purchaseOrder.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static PurchaseOrderDetailVO toDetailVO(PurchaseOrderSaveItemVO saveItemVO) {
        PurchaseOrderDetailVO detailVO = new PurchaseOrderDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
