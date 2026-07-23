package xbb.ai.erp.module.purchase.infrastructure.persistence.convertor;

import xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseRequestItemPO;

public final class PurchaseRequestItemConvertor {

    private PurchaseRequestItemConvertor() {
    }

    public static PurchaseRequestItemPO toPO(PurchaseRequestItem purchaseRequestItem) {
        if (purchaseRequestItem == null) {
            return null;
        }
        PurchaseRequestItemPO po = new PurchaseRequestItemPO();
        po.setId(purchaseRequestItem.getId());
        po.setCorpid(purchaseRequestItem.getCorpid());
        po.setRequestId(purchaseRequestItem.getRequestId());
        po.setLineNo(purchaseRequestItem.getLineNo());
        po.setSkuId(purchaseRequestItem.getSkuId());
        po.setSkuCodeSnapshot(purchaseRequestItem.getSkuCodeSnapshot());
        po.setSkuNameSnapshot(purchaseRequestItem.getSkuNameSnapshot());
        po.setSpecSnapshot(purchaseRequestItem.getSpecSnapshot());
        po.setPurchaseUnitId(purchaseRequestItem.getPurchaseUnitId());
        po.setRequestQty(purchaseRequestItem.getRequestQty());
        po.setReservedQty(purchaseRequestItem.getReservedQty());
        po.setExecutedQty(purchaseRequestItem.getExecutedQty());
        po.setClosedQty(purchaseRequestItem.getClosedQty());
        po.setSuggestedVendorId(purchaseRequestItem.getSuggestedVendorId());
        po.setSuggestedDeliveryDate(purchaseRequestItem.getSuggestedDeliveryDate());
        po.setVersion(purchaseRequestItem.getVersion());
        po.setDeleted(purchaseRequestItem.getDeleted());
        po.setAddTime(purchaseRequestItem.getAddTime());
        po.setUpdateTime(purchaseRequestItem.getUpdateTime());
        po.setCreatorId(purchaseRequestItem.getCreatorId());
        po.setModifyId(purchaseRequestItem.getModifyId());
        return po;
    }

    public static PurchaseRequestItem toDomain(PurchaseRequestItemPO po) {
        if (po == null) {
            return null;
        }
        PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
        purchaseRequestItem.setId(po.getId());
        purchaseRequestItem.setCorpid(po.getCorpid());
        purchaseRequestItem.setRequestId(po.getRequestId());
        purchaseRequestItem.setLineNo(po.getLineNo());
        purchaseRequestItem.setSkuId(po.getSkuId());
        purchaseRequestItem.setSkuCodeSnapshot(po.getSkuCodeSnapshot());
        purchaseRequestItem.setSkuNameSnapshot(po.getSkuNameSnapshot());
        purchaseRequestItem.setSpecSnapshot(po.getSpecSnapshot());
        purchaseRequestItem.setPurchaseUnitId(po.getPurchaseUnitId());
        purchaseRequestItem.setRequestQty(po.getRequestQty());
        purchaseRequestItem.setReservedQty(po.getReservedQty());
        purchaseRequestItem.setExecutedQty(po.getExecutedQty());
        purchaseRequestItem.setClosedQty(po.getClosedQty());
        purchaseRequestItem.setSuggestedVendorId(po.getSuggestedVendorId());
        purchaseRequestItem.setSuggestedDeliveryDate(po.getSuggestedDeliveryDate());
        purchaseRequestItem.setVersion(po.getVersion());
        purchaseRequestItem.setDeleted(po.getDeleted());
        purchaseRequestItem.setAddTime(po.getAddTime());
        purchaseRequestItem.setUpdateTime(po.getUpdateTime());
        purchaseRequestItem.setCreatorId(po.getCreatorId());
        purchaseRequestItem.setModifyId(po.getModifyId());
        return purchaseRequestItem;
    }
}
