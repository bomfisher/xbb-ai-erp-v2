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
        po.setInboundNo(purchaseInbound.getInboundNo());
        po.setPurchaseOrderId(purchaseInbound.getPurchaseOrderId());
        po.setSupplierId(purchaseInbound.getSupplierId());
        po.setSupplierName(purchaseInbound.getSupplierName());
        po.setWarehouseId(purchaseInbound.getWarehouseId());
        po.setInboundDate(purchaseInbound.getInboundDate());
        po.setTotalAmount(purchaseInbound.getTotalAmount());
        po.setStatus(purchaseInbound.getStatus());
        po.setAuditStatus(purchaseInbound.getAuditStatus());
        po.setRemark(purchaseInbound.getRemark());
        po.setCreatorId(purchaseInbound.getCreatorId());
        po.setModifyId(purchaseInbound.getModifyId());
        po.setAddTime(purchaseInbound.getAddTime());
        po.setUpdateTime(purchaseInbound.getUpdateTime());
        po.setDel(purchaseInbound.getDel());
        return po;
    }

    public static PurchaseInbound toDomain(PurchaseInboundPO po) {
        if (po == null) {
            return null;
        }
        PurchaseInbound purchaseInbound = new PurchaseInbound();
        purchaseInbound.setId(po.getId());
        purchaseInbound.setCorpid(po.getCorpid());
        purchaseInbound.setInboundNo(po.getInboundNo());
        purchaseInbound.setPurchaseOrderId(po.getPurchaseOrderId());
        purchaseInbound.setSupplierId(po.getSupplierId());
        purchaseInbound.setSupplierName(po.getSupplierName());
        purchaseInbound.setWarehouseId(po.getWarehouseId());
        purchaseInbound.setInboundDate(po.getInboundDate());
        purchaseInbound.setTotalAmount(po.getTotalAmount());
        purchaseInbound.setStatus(po.getStatus());
        purchaseInbound.setAuditStatus(po.getAuditStatus());
        purchaseInbound.setRemark(po.getRemark());
        purchaseInbound.setCreatorId(po.getCreatorId());
        purchaseInbound.setModifyId(po.getModifyId());
        purchaseInbound.setAddTime(po.getAddTime());
        purchaseInbound.setUpdateTime(po.getUpdateTime());
        purchaseInbound.setDel(po.getDel());
        return purchaseInbound;
    }
}
