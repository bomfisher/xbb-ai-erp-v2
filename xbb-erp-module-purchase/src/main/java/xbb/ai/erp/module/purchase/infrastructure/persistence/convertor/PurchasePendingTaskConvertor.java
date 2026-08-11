package xbb.ai.erp.module.purchase.infrastructure.persistence.convertor;

import xbb.ai.erp.module.purchase.domain.model.PurchasePendingTask;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchasePendingTaskPO;

public final class PurchasePendingTaskConvertor {

    private PurchasePendingTaskConvertor() {
    }

    public static PurchasePendingTaskPO toPO(PurchasePendingTask purchasePendingTask) {
        if (purchasePendingTask == null) {
            return null;
        }
        PurchasePendingTaskPO po = new PurchasePendingTaskPO();
        po.setId(purchasePendingTask.getId());
        po.setCorpid(purchasePendingTask.getCorpid());
        po.setPurchaseOrgId(purchasePendingTask.getPurchaseOrgId());
        po.setTaskNo(purchasePendingTask.getTaskNo());
        po.setSourceType(purchasePendingTask.getSourceType());
        po.setSourceDocId(purchasePendingTask.getSourceDocId());
        po.setSourceLineId(purchasePendingTask.getSourceLineId());
        po.setSourceDocNo(purchasePendingTask.getSourceDocNo());
        po.setSkuId(purchasePendingTask.getSkuId());
        po.setSkuCodeSnapshot(purchasePendingTask.getSkuCodeSnapshot());
        po.setSkuNameSnapshot(purchasePendingTask.getSkuNameSnapshot());
        po.setNeedQty(purchasePendingTask.getNeedQty());
        po.setOccupiedQty(purchasePendingTask.getOccupiedQty());
        po.setGeneratedRequestQty(purchasePendingTask.getGeneratedRequestQty());
        po.setGeneratedOrderQty(purchasePendingTask.getGeneratedOrderQty());
        po.setClosedQty(purchasePendingTask.getClosedQty());
        po.setSuggestedVendorId(purchasePendingTask.getSuggestedVendorId());
        po.setSuggestedDeliveryDate(purchasePendingTask.getSuggestedDeliveryDate());
        po.setPriorityLevel(purchasePendingTask.getPriorityLevel());
        po.setTaskStatus(purchasePendingTask.getTaskStatus());
        po.setSalesLinkedFlag(purchasePendingTask.getSalesLinkedFlag());
        po.setVersion(purchasePendingTask.getVersion());
        po.setDel(purchasePendingTask.getDeleted());
        po.setAddTime(purchasePendingTask.getAddTime());
        po.setUpdateTime(purchasePendingTask.getUpdateTime());
        po.setCreatorId(purchasePendingTask.getCreatorId());
        po.setModifyId(purchasePendingTask.getModifyId());
        return po;
    }

    public static PurchasePendingTask toDomain(PurchasePendingTaskPO po) {
        if (po == null) {
            return null;
        }
        PurchasePendingTask purchasePendingTask = new PurchasePendingTask();
        purchasePendingTask.setId(po.getId());
        purchasePendingTask.setCorpid(po.getCorpid());
        purchasePendingTask.setPurchaseOrgId(po.getPurchaseOrgId());
        purchasePendingTask.setTaskNo(po.getTaskNo());
        purchasePendingTask.setSourceType(po.getSourceType());
        purchasePendingTask.setSourceDocId(po.getSourceDocId());
        purchasePendingTask.setSourceLineId(po.getSourceLineId());
        purchasePendingTask.setSourceDocNo(po.getSourceDocNo());
        purchasePendingTask.setSkuId(po.getSkuId());
        purchasePendingTask.setSkuCodeSnapshot(po.getSkuCodeSnapshot());
        purchasePendingTask.setSkuNameSnapshot(po.getSkuNameSnapshot());
        purchasePendingTask.setNeedQty(po.getNeedQty());
        purchasePendingTask.setOccupiedQty(po.getOccupiedQty());
        purchasePendingTask.setGeneratedRequestQty(po.getGeneratedRequestQty());
        purchasePendingTask.setGeneratedOrderQty(po.getGeneratedOrderQty());
        purchasePendingTask.setClosedQty(po.getClosedQty());
        purchasePendingTask.setSuggestedVendorId(po.getSuggestedVendorId());
        purchasePendingTask.setSuggestedDeliveryDate(po.getSuggestedDeliveryDate());
        purchasePendingTask.setPriorityLevel(po.getPriorityLevel());
        purchasePendingTask.setTaskStatus(po.getTaskStatus());
        purchasePendingTask.setSalesLinkedFlag(po.getSalesLinkedFlag());
        purchasePendingTask.setVersion(po.getVersion());
        purchasePendingTask.setDeleted(po.getDel());
        purchasePendingTask.setAddTime(po.getAddTime());
        purchasePendingTask.setUpdateTime(po.getUpdateTime());
        purchasePendingTask.setCreatorId(po.getCreatorId());
        purchasePendingTask.setModifyId(po.getModifyId());
        return purchasePendingTask;
    }
}
