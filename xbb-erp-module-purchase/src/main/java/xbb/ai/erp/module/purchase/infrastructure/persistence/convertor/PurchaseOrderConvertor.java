package xbb.ai.erp.module.purchase.infrastructure.persistence.convertor;

import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseOrderPO;

public final class PurchaseOrderConvertor {

    private PurchaseOrderConvertor() {
    }

    public static PurchaseOrderPO toPO(PurchaseOrder purchaseOrder) {
        if (purchaseOrder == null) {
            return null;
        }
        PurchaseOrderPO po = new PurchaseOrderPO();
        po.setId(purchaseOrder.getId());
        po.setCorpid(purchaseOrder.getCorpid());
        po.setPurchaseOrgId(purchaseOrder.getPurchaseOrgId());
        po.setOrderNo(purchaseOrder.getOrderNo());
        po.setVendorId(purchaseOrder.getVendorId());
        po.setVendorNameSnapshot(purchaseOrder.getVendorNameSnapshot());
        po.setPurchaserId(purchaseOrder.getPurchaserId());
        po.setPurchaserNameSnapshot(purchaseOrder.getPurchaserNameSnapshot());
        po.setWarehouseId(purchaseOrder.getWarehouseId());
        po.setWarehouseNameSnapshot(purchaseOrder.getWarehouseNameSnapshot());
        po.setSettlementMethodId(purchaseOrder.getSettlementMethodId());
        po.setSettlementMethodSnapshot(purchaseOrder.getSettlementMethodSnapshot());
        po.setPaymentTermSnapshot(purchaseOrder.getPaymentTermSnapshot());
        po.setCurrencyCode(purchaseOrder.getCurrencyCode());
        po.setDeliveryDate(purchaseOrder.getDeliveryDate());
        po.setSourceType(purchaseOrder.getSourceType());
        po.setSourceNo(purchaseOrder.getSourceNo());
        po.setSalesLinkedFlag(purchaseOrder.getSalesLinkedFlag());
        po.setBizStatus(purchaseOrder.getBizStatus());
        po.setApprovalStatus(purchaseOrder.getApprovalStatus());
        po.setExecutionStatus(purchaseOrder.getExecutionStatus());
        po.setReceiptStatus(purchaseOrder.getReceiptStatus());
        po.setInboundStatus(purchaseOrder.getInboundStatus());
        po.setPayableStatus(purchaseOrder.getPayableStatus());
        po.setInvoiceStatus(purchaseOrder.getInvoiceStatus());
        po.setPaymentStatus(purchaseOrder.getPaymentStatus());
        po.setGrossAmount(purchaseOrder.getGrossAmount());
        po.setNetAmount(purchaseOrder.getNetAmount());
        po.setTaxAmount(purchaseOrder.getTaxAmount());
        po.setInboundedQtySummary(purchaseOrder.getInboundedQtySummary());
        po.setUninboundedQtySummary(purchaseOrder.getUninboundedQtySummary());
        po.setClosedQtySummary(purchaseOrder.getClosedQtySummary());
        po.setPayableAmountSummary(purchaseOrder.getPayableAmountSummary());
        po.setPaidAmountSummary(purchaseOrder.getPaidAmountSummary());
        po.setInvoicedAmountSummary(purchaseOrder.getInvoicedAmountSummary());
        po.setLastInboundTime(purchaseOrder.getLastInboundTime());
        po.setLastPayableTime(purchaseOrder.getLastPayableTime());
        po.setPeriodLockedFlag(purchaseOrder.getPeriodLockedFlag());
        po.setVersion(purchaseOrder.getVersion());
        po.setRemark(purchaseOrder.getRemark());
        po.setDeleted(purchaseOrder.getDeleted());
        po.setAddTime(purchaseOrder.getAddTime());
        po.setUpdateTime(purchaseOrder.getUpdateTime());
        po.setCreatorId(purchaseOrder.getCreatorId());
        po.setModifyId(purchaseOrder.getModifyId());
        return po;
    }

    public static PurchaseOrder toDomain(PurchaseOrderPO po) {
        if (po == null) {
            return null;
        }
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(po.getId());
        purchaseOrder.setCorpid(po.getCorpid());
        purchaseOrder.setPurchaseOrgId(po.getPurchaseOrgId());
        purchaseOrder.setOrderNo(po.getOrderNo());
        purchaseOrder.setVendorId(po.getVendorId());
        purchaseOrder.setVendorNameSnapshot(po.getVendorNameSnapshot());
        purchaseOrder.setPurchaserId(po.getPurchaserId());
        purchaseOrder.setPurchaserNameSnapshot(po.getPurchaserNameSnapshot());
        purchaseOrder.setWarehouseId(po.getWarehouseId());
        purchaseOrder.setWarehouseNameSnapshot(po.getWarehouseNameSnapshot());
        purchaseOrder.setSettlementMethodId(po.getSettlementMethodId());
        purchaseOrder.setSettlementMethodSnapshot(po.getSettlementMethodSnapshot());
        purchaseOrder.setPaymentTermSnapshot(po.getPaymentTermSnapshot());
        purchaseOrder.setCurrencyCode(po.getCurrencyCode());
        purchaseOrder.setDeliveryDate(po.getDeliveryDate());
        purchaseOrder.setSourceType(po.getSourceType());
        purchaseOrder.setSourceNo(po.getSourceNo());
        purchaseOrder.setSalesLinkedFlag(po.getSalesLinkedFlag());
        purchaseOrder.setBizStatus(po.getBizStatus());
        purchaseOrder.setApprovalStatus(po.getApprovalStatus());
        purchaseOrder.setExecutionStatus(po.getExecutionStatus());
        purchaseOrder.setReceiptStatus(po.getReceiptStatus());
        purchaseOrder.setInboundStatus(po.getInboundStatus());
        purchaseOrder.setPayableStatus(po.getPayableStatus());
        purchaseOrder.setInvoiceStatus(po.getInvoiceStatus());
        purchaseOrder.setPaymentStatus(po.getPaymentStatus());
        purchaseOrder.setGrossAmount(po.getGrossAmount());
        purchaseOrder.setNetAmount(po.getNetAmount());
        purchaseOrder.setTaxAmount(po.getTaxAmount());
        purchaseOrder.setInboundedQtySummary(po.getInboundedQtySummary());
        purchaseOrder.setUninboundedQtySummary(po.getUninboundedQtySummary());
        purchaseOrder.setClosedQtySummary(po.getClosedQtySummary());
        purchaseOrder.setPayableAmountSummary(po.getPayableAmountSummary());
        purchaseOrder.setPaidAmountSummary(po.getPaidAmountSummary());
        purchaseOrder.setInvoicedAmountSummary(po.getInvoicedAmountSummary());
        purchaseOrder.setLastInboundTime(po.getLastInboundTime());
        purchaseOrder.setLastPayableTime(po.getLastPayableTime());
        purchaseOrder.setPeriodLockedFlag(po.getPeriodLockedFlag());
        purchaseOrder.setVersion(po.getVersion());
        purchaseOrder.setRemark(po.getRemark());
        purchaseOrder.setDeleted(po.getDeleted());
        purchaseOrder.setAddTime(po.getAddTime());
        purchaseOrder.setUpdateTime(po.getUpdateTime());
        purchaseOrder.setCreatorId(po.getCreatorId());
        purchaseOrder.setModifyId(po.getModifyId());
        return purchaseOrder;
    }
}
