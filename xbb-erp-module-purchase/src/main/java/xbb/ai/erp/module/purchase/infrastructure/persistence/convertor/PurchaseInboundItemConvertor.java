package xbb.ai.erp.module.purchase.infrastructure.persistence.convertor;

import xbb.ai.erp.module.purchase.domain.model.PurchaseInboundItem;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseInboundItemPO;

public final class PurchaseInboundItemConvertor {

    private PurchaseInboundItemConvertor() {
    }

    public static PurchaseInboundItemPO toPO(PurchaseInboundItem purchaseInboundItem) {
        if (purchaseInboundItem == null) {
            return null;
        }
        PurchaseInboundItemPO po = new PurchaseInboundItemPO();
        po.setId(purchaseInboundItem.getId());
        po.setCorpid(purchaseInboundItem.getCorpid());
        po.setPurchaseInboundId(purchaseInboundItem.getPurchaseInboundId());
        po.setPurchaseOrderItemId(purchaseInboundItem.getPurchaseOrderItemId());
        po.setSkuId(purchaseInboundItem.getSkuId());
        po.setSkuName(purchaseInboundItem.getSkuName());
        po.setUnitName(purchaseInboundItem.getUnitName());
        po.setWarehouseId(purchaseInboundItem.getWarehouseId());
        po.setQty(purchaseInboundItem.getQty());
        po.setUnitPrice(purchaseInboundItem.getUnitPrice());
        po.setAmount(purchaseInboundItem.getAmount());
        po.setCostUnit(purchaseInboundItem.getCostUnit());
        po.setCostAmount(purchaseInboundItem.getCostAmount());
        po.setCreatorId(purchaseInboundItem.getCreatorId());
        po.setModifyId(purchaseInboundItem.getModifyId());
        return po;
    }

    public static PurchaseInboundItem toDomain(PurchaseInboundItemPO po) {
        if (po == null) {
            return null;
        }
        PurchaseInboundItem purchaseInboundItem = new PurchaseInboundItem();
        purchaseInboundItem.setId(po.getId());
        purchaseInboundItem.setCorpid(po.getCorpid());
        purchaseInboundItem.setPurchaseInboundId(po.getPurchaseInboundId());
        purchaseInboundItem.setPurchaseOrderItemId(po.getPurchaseOrderItemId());
        purchaseInboundItem.setSkuId(po.getSkuId());
        purchaseInboundItem.setSkuName(po.getSkuName());
        purchaseInboundItem.setUnitName(po.getUnitName());
        purchaseInboundItem.setWarehouseId(po.getWarehouseId());
        purchaseInboundItem.setQty(po.getQty());
        purchaseInboundItem.setUnitPrice(po.getUnitPrice());
        purchaseInboundItem.setAmount(po.getAmount());
        purchaseInboundItem.setCostUnit(po.getCostUnit());
        purchaseInboundItem.setCostAmount(po.getCostAmount());
        purchaseInboundItem.setCreatorId(po.getCreatorId());
        purchaseInboundItem.setModifyId(po.getModifyId());
        return purchaseInboundItem;
    }
}
