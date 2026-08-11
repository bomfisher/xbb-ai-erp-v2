package xbb.ai.erp.module.purchase.infrastructure.persistence.convertor;

import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseOrderItemPO;

public final class PurchaseOrderItemConvertor {

    private PurchaseOrderItemConvertor() {
    }

    public static PurchaseOrderItemPO toPO(PurchaseOrderItem purchaseOrderItem) {
        if (purchaseOrderItem == null) {
            return null;
        }
        PurchaseOrderItemPO po = new PurchaseOrderItemPO();
        po.setId(purchaseOrderItem.getId());
        po.setCorpid(purchaseOrderItem.getCorpid());
        po.setOrderId(purchaseOrderItem.getOrderId());
        po.setLineNo(purchaseOrderItem.getLineNo());
        po.setSkuId(purchaseOrderItem.getSkuId());
        po.setSkuCodeSnapshot(purchaseOrderItem.getSkuCodeSnapshot());
        po.setSkuNameSnapshot(purchaseOrderItem.getSkuNameSnapshot());
        po.setSpecSnapshot(purchaseOrderItem.getSpecSnapshot());
        po.setPurchaseUnitId(purchaseOrderItem.getPurchaseUnitId());
        po.setWarehouseId(purchaseOrderItem.getWarehouseId());
        po.setOrderQty(purchaseOrderItem.getOrderQty());
        po.setReceivedQty(purchaseOrderItem.getReceivedQty());
        po.setInboundedQty(purchaseOrderItem.getInboundedQty());
        po.setClosedQty(purchaseOrderItem.getClosedQty());
        po.setReturnedQty(purchaseOrderItem.getReturnedQty());
        po.setGrossPrice(purchaseOrderItem.getGrossPrice());
        po.setNetPrice(purchaseOrderItem.getNetPrice());
        po.setTaxRate(purchaseOrderItem.getTaxRate());
        po.setTaxAmount(purchaseOrderItem.getTaxAmount());
        po.setGrossAmount(purchaseOrderItem.getGrossAmount());
        po.setNetAmount(purchaseOrderItem.getNetAmount());
        po.setPayableAmount(purchaseOrderItem.getPayableAmount());
        po.setPaidAmount(purchaseOrderItem.getPaidAmount());
        po.setInvoicedAmount(purchaseOrderItem.getInvoicedAmount());
        po.setIsGift(purchaseOrderItem.getIsGift());
        po.setDeliveryPlanSnapshot(purchaseOrderItem.getDeliveryPlanSnapshot());
        po.setVersion(purchaseOrderItem.getVersion());
        po.setDel(purchaseOrderItem.getDeleted());
        po.setAddTime(purchaseOrderItem.getAddTime());
        po.setUpdateTime(purchaseOrderItem.getUpdateTime());
        po.setCreatorId(purchaseOrderItem.getCreatorId());
        po.setModifyId(purchaseOrderItem.getModifyId());
        return po;
    }

    public static PurchaseOrderItem toDomain(PurchaseOrderItemPO po) {
        if (po == null) {
            return null;
        }
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
        purchaseOrderItem.setId(po.getId());
        purchaseOrderItem.setCorpid(po.getCorpid());
        purchaseOrderItem.setOrderId(po.getOrderId());
        purchaseOrderItem.setLineNo(po.getLineNo());
        purchaseOrderItem.setSkuId(po.getSkuId());
        purchaseOrderItem.setSkuCodeSnapshot(po.getSkuCodeSnapshot());
        purchaseOrderItem.setSkuNameSnapshot(po.getSkuNameSnapshot());
        purchaseOrderItem.setSpecSnapshot(po.getSpecSnapshot());
        purchaseOrderItem.setPurchaseUnitId(po.getPurchaseUnitId());
        purchaseOrderItem.setWarehouseId(po.getWarehouseId());
        purchaseOrderItem.setOrderQty(po.getOrderQty());
        purchaseOrderItem.setReceivedQty(po.getReceivedQty());
        purchaseOrderItem.setInboundedQty(po.getInboundedQty());
        purchaseOrderItem.setClosedQty(po.getClosedQty());
        purchaseOrderItem.setReturnedQty(po.getReturnedQty());
        purchaseOrderItem.setGrossPrice(po.getGrossPrice());
        purchaseOrderItem.setNetPrice(po.getNetPrice());
        purchaseOrderItem.setTaxRate(po.getTaxRate());
        purchaseOrderItem.setTaxAmount(po.getTaxAmount());
        purchaseOrderItem.setGrossAmount(po.getGrossAmount());
        purchaseOrderItem.setNetAmount(po.getNetAmount());
        purchaseOrderItem.setPayableAmount(po.getPayableAmount());
        purchaseOrderItem.setPaidAmount(po.getPaidAmount());
        purchaseOrderItem.setInvoicedAmount(po.getInvoicedAmount());
        purchaseOrderItem.setIsGift(po.getIsGift());
        purchaseOrderItem.setDeliveryPlanSnapshot(po.getDeliveryPlanSnapshot());
        purchaseOrderItem.setVersion(po.getVersion());
        purchaseOrderItem.setDeleted(po.getDel());
        purchaseOrderItem.setAddTime(po.getAddTime());
        purchaseOrderItem.setUpdateTime(po.getUpdateTime());
        purchaseOrderItem.setCreatorId(po.getCreatorId());
        purchaseOrderItem.setModifyId(po.getModifyId());
        return purchaseOrderItem;
    }
}
