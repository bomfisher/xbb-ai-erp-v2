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
        po.setInboundId(purchaseInboundItem.getInboundId());
        po.setLineNo(purchaseInboundItem.getLineNo());
        po.setSourceLineId(purchaseInboundItem.getSourceLineId());
        po.setSkuId(purchaseInboundItem.getSkuId());
        po.setWarehouseId(purchaseInboundItem.getWarehouseId());
        po.setBatchNo(purchaseInboundItem.getBatchNo());
        po.setSerialNo(purchaseInboundItem.getSerialNo());
        po.setProduceDate(purchaseInboundItem.getProduceDate());
        po.setExpireDate(purchaseInboundItem.getExpireDate());
        po.setQty(purchaseInboundItem.getQty());
        po.setQualifiedQty(purchaseInboundItem.getQualifiedQty());
        po.setUnqualifiedQty(purchaseInboundItem.getUnqualifiedQty());
        po.setGrossPrice(purchaseInboundItem.getGrossPrice());
        po.setNetPrice(purchaseInboundItem.getNetPrice());
        po.setTaxRate(purchaseInboundItem.getTaxRate());
        po.setGrossAmount(purchaseInboundItem.getGrossAmount());
        po.setNetAmount(purchaseInboundItem.getNetAmount());
        po.setTaxAmount(purchaseInboundItem.getTaxAmount());
        po.setInventoryCostAmount(purchaseInboundItem.getInventoryCostAmount());
        po.setVersion(purchaseInboundItem.getVersion());
        po.setDeleted(purchaseInboundItem.getDeleted());
        po.setAddTime(purchaseInboundItem.getAddTime());
        po.setUpdateTime(purchaseInboundItem.getUpdateTime());
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
        purchaseInboundItem.setInboundId(po.getInboundId());
        purchaseInboundItem.setLineNo(po.getLineNo());
        purchaseInboundItem.setSourceLineId(po.getSourceLineId());
        purchaseInboundItem.setSkuId(po.getSkuId());
        purchaseInboundItem.setWarehouseId(po.getWarehouseId());
        purchaseInboundItem.setBatchNo(po.getBatchNo());
        purchaseInboundItem.setSerialNo(po.getSerialNo());
        purchaseInboundItem.setProduceDate(po.getProduceDate());
        purchaseInboundItem.setExpireDate(po.getExpireDate());
        purchaseInboundItem.setQty(po.getQty());
        purchaseInboundItem.setQualifiedQty(po.getQualifiedQty());
        purchaseInboundItem.setUnqualifiedQty(po.getUnqualifiedQty());
        purchaseInboundItem.setGrossPrice(po.getGrossPrice());
        purchaseInboundItem.setNetPrice(po.getNetPrice());
        purchaseInboundItem.setTaxRate(po.getTaxRate());
        purchaseInboundItem.setGrossAmount(po.getGrossAmount());
        purchaseInboundItem.setNetAmount(po.getNetAmount());
        purchaseInboundItem.setTaxAmount(po.getTaxAmount());
        purchaseInboundItem.setInventoryCostAmount(po.getInventoryCostAmount());
        purchaseInboundItem.setVersion(po.getVersion());
        purchaseInboundItem.setDeleted(po.getDeleted());
        purchaseInboundItem.setAddTime(po.getAddTime());
        purchaseInboundItem.setUpdateTime(po.getUpdateTime());
        purchaseInboundItem.setCreatorId(po.getCreatorId());
        purchaseInboundItem.setModifyId(po.getModifyId());
        return purchaseInboundItem;
    }
}
