package xbb.ai.erp.module.purchase.infrastructure.persistence.convertor;

import xbb.ai.erp.module.purchase.domain.model.PurchaseSourceRelation;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseSourceRelationPO;

public final class PurchaseSourceRelationConvertor {

    private PurchaseSourceRelationConvertor() {
    }

    public static PurchaseSourceRelationPO toPO(PurchaseSourceRelation purchaseSourceRelation) {
        if (purchaseSourceRelation == null) {
            return null;
        }
        PurchaseSourceRelationPO po = new PurchaseSourceRelationPO();
        po.setId(purchaseSourceRelation.getId());
        po.setCorpid(purchaseSourceRelation.getCorpid());
        po.setSourceDocType(purchaseSourceRelation.getSourceDocType());
        po.setSourceDocId(purchaseSourceRelation.getSourceDocId());
        po.setSourceLineId(purchaseSourceRelation.getSourceLineId());
        po.setTargetDocType(purchaseSourceRelation.getTargetDocType());
        po.setTargetDocId(purchaseSourceRelation.getTargetDocId());
        po.setTargetLineId(purchaseSourceRelation.getTargetLineId());
        po.setSourceQty(purchaseSourceRelation.getSourceQty());
        po.setReservedQty(purchaseSourceRelation.getReservedQty());
        po.setExecutedQty(purchaseSourceRelation.getExecutedQty());
        po.setClosedQty(purchaseSourceRelation.getClosedQty());
        po.setReversedQty(purchaseSourceRelation.getReversedQty());
        po.setRelationStatus(purchaseSourceRelation.getRelationStatus());
        po.setVersion(purchaseSourceRelation.getVersion());
        po.setDel(purchaseSourceRelation.getDeleted());
        po.setAddTime(purchaseSourceRelation.getAddTime());
        po.setUpdateTime(purchaseSourceRelation.getUpdateTime());
        po.setCreatorId(purchaseSourceRelation.getCreatorId());
        po.setModifyId(purchaseSourceRelation.getModifyId());
        return po;
    }

    public static PurchaseSourceRelation toDomain(PurchaseSourceRelationPO po) {
        if (po == null) {
            return null;
        }
        PurchaseSourceRelation purchaseSourceRelation = new PurchaseSourceRelation();
        purchaseSourceRelation.setId(po.getId());
        purchaseSourceRelation.setCorpid(po.getCorpid());
        purchaseSourceRelation.setSourceDocType(po.getSourceDocType());
        purchaseSourceRelation.setSourceDocId(po.getSourceDocId());
        purchaseSourceRelation.setSourceLineId(po.getSourceLineId());
        purchaseSourceRelation.setTargetDocType(po.getTargetDocType());
        purchaseSourceRelation.setTargetDocId(po.getTargetDocId());
        purchaseSourceRelation.setTargetLineId(po.getTargetLineId());
        purchaseSourceRelation.setSourceQty(po.getSourceQty());
        purchaseSourceRelation.setReservedQty(po.getReservedQty());
        purchaseSourceRelation.setExecutedQty(po.getExecutedQty());
        purchaseSourceRelation.setClosedQty(po.getClosedQty());
        purchaseSourceRelation.setReversedQty(po.getReversedQty());
        purchaseSourceRelation.setRelationStatus(po.getRelationStatus());
        purchaseSourceRelation.setVersion(po.getVersion());
        purchaseSourceRelation.setDeleted(po.getDel());
        purchaseSourceRelation.setAddTime(po.getAddTime());
        purchaseSourceRelation.setUpdateTime(po.getUpdateTime());
        purchaseSourceRelation.setCreatorId(po.getCreatorId());
        purchaseSourceRelation.setModifyId(po.getModifyId());
        return purchaseSourceRelation;
    }
}
