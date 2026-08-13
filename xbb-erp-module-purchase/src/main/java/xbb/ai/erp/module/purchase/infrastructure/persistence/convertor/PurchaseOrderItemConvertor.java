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
        po.setPurchaseOrderId(purchaseOrderItem.getPurchaseOrderId());
        po.setLineNo(purchaseOrderItem.getLineNo());
        po.setSkuId(purchaseOrderItem.getSkuId());
        po.setSkuCode(purchaseOrderItem.getSkuCode());
        po.setSkuName(purchaseOrderItem.getSkuName());
        po.setSpecification(purchaseOrderItem.getSpecification());
        po.setUnitName(purchaseOrderItem.getUnitName());
        po.setQty(purchaseOrderItem.getQty());
        po.setInboundQty(purchaseOrderItem.getInboundQty());
        po.setUnitPrice(purchaseOrderItem.getUnitPrice());
        po.setTaxRate(purchaseOrderItem.getTaxRate());
        po.setAmount(purchaseOrderItem.getAmount());
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
        purchaseOrderItem.setPurchaseOrderId(po.getPurchaseOrderId());
        purchaseOrderItem.setLineNo(po.getLineNo());
        purchaseOrderItem.setSkuId(po.getSkuId());
        purchaseOrderItem.setSkuCode(po.getSkuCode());
        purchaseOrderItem.setSkuName(po.getSkuName());
        purchaseOrderItem.setSpecification(po.getSpecification());
        purchaseOrderItem.setUnitName(po.getUnitName());
        purchaseOrderItem.setQty(po.getQty());
        purchaseOrderItem.setInboundQty(po.getInboundQty());
        purchaseOrderItem.setUnitPrice(po.getUnitPrice());
        purchaseOrderItem.setTaxRate(po.getTaxRate());
        purchaseOrderItem.setAmount(po.getAmount());
        purchaseOrderItem.setCreatorId(po.getCreatorId());
        purchaseOrderItem.setModifyId(po.getModifyId());
        return purchaseOrderItem;
    }
}
