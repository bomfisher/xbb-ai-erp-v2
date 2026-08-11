package xbb.ai.erp.module.purchase.infrastructure.persistence.convertor;

import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInboundPO;

public final class PurchaseInboundConvertor {

    private PurchaseInboundConvertor() {
    }

    public static PurchaseInboundPO toPO(PurchaseInbound purchaseInbound) {
        if (purchaseInbound == null) {
            return null;
        }
        PurchaseInboundPO po = new PurchaseInboundPO();
        po.setId(purchaseInbound.getId());
        po.setCorpid(purchaseInbound.getCorpid());
        po.setPurchaseOrgId(purchaseInbound.getPurchaseOrgId());
        po.setInboundNo(purchaseInbound.getInboundNo());
        po.setSourceDocType(purchaseInbound.getSourceDocType());
        po.setSourceDocId(purchaseInbound.getSourceDocId());
        po.setVendorId(purchaseInbound.getVendorId());
        po.setWarehouseId(purchaseInbound.getWarehouseId());
        po.setActualInboundTime(purchaseInbound.getActualInboundTime());
        po.setBizStatus(purchaseInbound.getBizStatus());
        po.setApprovalStatus(purchaseInbound.getApprovalStatus());
        po.setExecutionStatus(purchaseInbound.getExecutionStatus());
        po.setGrossAmount(purchaseInbound.getGrossAmount());
        po.setNetAmount(purchaseInbound.getNetAmount());
        po.setTaxAmount(purchaseInbound.getTaxAmount());
        po.setInventoryCostAmount(purchaseInbound.getInventoryCostAmount());
        po.setInventoryFlowNo(purchaseInbound.getInventoryFlowNo());
        po.setPayableTriggerStatus(purchaseInbound.getPayableTriggerStatus());
        po.setPayableNo(purchaseInbound.getPayableNo());
        po.setInvoiceSourceStatus(purchaseInbound.getInvoiceSourceStatus());
        po.setPeriodLockedFlag(purchaseInbound.getPeriodLockedFlag());
        po.setVersion(purchaseInbound.getVersion());
        po.setRemark(purchaseInbound.getRemark());
        po.setDel(purchaseInbound.getDeleted());
        po.setAddTime(purchaseInbound.getAddTime());
        po.setUpdateTime(purchaseInbound.getUpdateTime());
        po.setCreatorId(purchaseInbound.getCreatorId());
        po.setModifyId(purchaseInbound.getModifyId());
        return po;
    }

    public static PurchaseInbound toDomain(PurchaseInboundPO po) {
        if (po == null) {
            return null;
        }
        PurchaseInbound purchaseInbound = new PurchaseInbound();
        purchaseInbound.setId(po.getId());
        purchaseInbound.setCorpid(po.getCorpid());
        purchaseInbound.setPurchaseOrgId(po.getPurchaseOrgId());
        purchaseInbound.setInboundNo(po.getInboundNo());
        purchaseInbound.setSourceDocType(po.getSourceDocType());
        purchaseInbound.setSourceDocId(po.getSourceDocId());
        purchaseInbound.setVendorId(po.getVendorId());
        purchaseInbound.setWarehouseId(po.getWarehouseId());
        purchaseInbound.setActualInboundTime(po.getActualInboundTime());
        purchaseInbound.setBizStatus(po.getBizStatus());
        purchaseInbound.setApprovalStatus(po.getApprovalStatus());
        purchaseInbound.setExecutionStatus(po.getExecutionStatus());
        purchaseInbound.setGrossAmount(po.getGrossAmount());
        purchaseInbound.setNetAmount(po.getNetAmount());
        purchaseInbound.setTaxAmount(po.getTaxAmount());
        purchaseInbound.setInventoryCostAmount(po.getInventoryCostAmount());
        purchaseInbound.setInventoryFlowNo(po.getInventoryFlowNo());
        purchaseInbound.setPayableTriggerStatus(po.getPayableTriggerStatus());
        purchaseInbound.setPayableNo(po.getPayableNo());
        purchaseInbound.setInvoiceSourceStatus(po.getInvoiceSourceStatus());
        purchaseInbound.setPeriodLockedFlag(po.getPeriodLockedFlag());
        purchaseInbound.setVersion(po.getVersion());
        purchaseInbound.setRemark(po.getRemark());
        purchaseInbound.setDeleted(po.getDel());
        purchaseInbound.setAddTime(po.getAddTime());
        purchaseInbound.setUpdateTime(po.getUpdateTime());
        purchaseInbound.setCreatorId(po.getCreatorId());
        purchaseInbound.setModifyId(po.getModifyId());
        return purchaseInbound;
    }
}
