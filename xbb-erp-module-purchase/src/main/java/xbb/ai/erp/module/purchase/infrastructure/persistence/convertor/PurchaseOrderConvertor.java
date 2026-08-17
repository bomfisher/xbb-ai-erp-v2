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
        po.setOrderNo(purchaseOrder.getOrderNo());
        po.setSupplierId(purchaseOrder.getSupplierId());
        po.setSupplierName(purchaseOrder.getSupplierName());
        po.setOrderDate(purchaseOrder.getOrderDate());
        po.setExpectedDate(purchaseOrder.getExpectedDate());
        po.setTotalAmount(purchaseOrder.getTotalAmount());
        po.setStatus(purchaseOrder.getStatus());
        po.setAuditStatus(purchaseOrder.getAuditStatus());
        po.setInboundStatus(purchaseOrder.getInboundStatus());
        po.setPaymentStatus(purchaseOrder.getPaymentStatus());
        po.setRemark(purchaseOrder.getRemark());
        po.setCreatorId(purchaseOrder.getCreatorId());
        po.setModifyId(purchaseOrder.getModifyId());
        po.setAddTime(purchaseOrder.getAddTime());
        po.setUpdateTime(purchaseOrder.getUpdateTime());
        po.setDel(purchaseOrder.getDel());
        return po;
    }

    public static PurchaseOrder toDomain(PurchaseOrderPO po) {
        if (po == null) {
            return null;
        }
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        purchaseOrder.setId(po.getId());
        purchaseOrder.setCorpid(po.getCorpid());
        purchaseOrder.setOrderNo(po.getOrderNo());
        purchaseOrder.setSupplierId(po.getSupplierId());
        purchaseOrder.setSupplierName(po.getSupplierName());
        purchaseOrder.setOrderDate(po.getOrderDate());
        purchaseOrder.setExpectedDate(po.getExpectedDate());
        purchaseOrder.setTotalAmount(po.getTotalAmount());
        purchaseOrder.setStatus(po.getStatus());
        purchaseOrder.setAuditStatus(po.getAuditStatus());
        purchaseOrder.setInboundStatus(po.getInboundStatus());
        purchaseOrder.setPaymentStatus(po.getPaymentStatus());
        purchaseOrder.setRemark(po.getRemark());
        purchaseOrder.setCreatorId(po.getCreatorId());
        purchaseOrder.setModifyId(po.getModifyId());
        purchaseOrder.setAddTime(po.getAddTime());
        purchaseOrder.setUpdateTime(po.getUpdateTime());
        purchaseOrder.setDel(po.getDel());
        return purchaseOrder;
    }
}
