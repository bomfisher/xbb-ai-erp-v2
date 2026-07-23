package xbb.ai.erp.module.purchase.infrastructure.persistence.convertor;

import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchaseRequestPO;

public final class PurchaseRequestConvertor {

    private PurchaseRequestConvertor() {
    }

    public static PurchaseRequestPO toPO(PurchaseRequest purchaseRequest) {
        if (purchaseRequest == null) {
            return null;
        }
        PurchaseRequestPO po = new PurchaseRequestPO();
        po.setId(purchaseRequest.getId());
        po.setCorpid(purchaseRequest.getCorpid());
        po.setPurchaseOrgId(purchaseRequest.getPurchaseOrgId());
        po.setRequestNo(purchaseRequest.getRequestNo());
        po.setRequestDeptId(purchaseRequest.getRequestDeptId());
        po.setApplicantId(purchaseRequest.getApplicantId());
        po.setSourceType(purchaseRequest.getSourceType());
        po.setSourceNo(purchaseRequest.getSourceNo());
        po.setSuggestedVendorId(purchaseRequest.getSuggestedVendorId());
        po.setSuggestedDeliveryDate(purchaseRequest.getSuggestedDeliveryDate());
        po.setBizStatus(purchaseRequest.getBizStatus());
        po.setApprovalStatus(purchaseRequest.getApprovalStatus());
        po.setGrossAmount(purchaseRequest.getGrossAmount());
        po.setNetAmount(purchaseRequest.getNetAmount());
        po.setTaxAmount(purchaseRequest.getTaxAmount());
        po.setVersion(purchaseRequest.getVersion());
        po.setRemark(purchaseRequest.getRemark());
        po.setDeleted(purchaseRequest.getDeleted());
        po.setAddTime(purchaseRequest.getAddTime());
        po.setUpdateTime(purchaseRequest.getUpdateTime());
        po.setCreatorId(purchaseRequest.getCreatorId());
        po.setModifyId(purchaseRequest.getModifyId());
        return po;
    }

    public static PurchaseRequest toDomain(PurchaseRequestPO po) {
        if (po == null) {
            return null;
        }
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        purchaseRequest.setId(po.getId());
        purchaseRequest.setCorpid(po.getCorpid());
        purchaseRequest.setPurchaseOrgId(po.getPurchaseOrgId());
        purchaseRequest.setRequestNo(po.getRequestNo());
        purchaseRequest.setRequestDeptId(po.getRequestDeptId());
        purchaseRequest.setApplicantId(po.getApplicantId());
        purchaseRequest.setSourceType(po.getSourceType());
        purchaseRequest.setSourceNo(po.getSourceNo());
        purchaseRequest.setSuggestedVendorId(po.getSuggestedVendorId());
        purchaseRequest.setSuggestedDeliveryDate(po.getSuggestedDeliveryDate());
        purchaseRequest.setBizStatus(po.getBizStatus());
        purchaseRequest.setApprovalStatus(po.getApprovalStatus());
        purchaseRequest.setGrossAmount(po.getGrossAmount());
        purchaseRequest.setNetAmount(po.getNetAmount());
        purchaseRequest.setTaxAmount(po.getTaxAmount());
        purchaseRequest.setVersion(po.getVersion());
        purchaseRequest.setRemark(po.getRemark());
        purchaseRequest.setDeleted(po.getDeleted());
        purchaseRequest.setAddTime(po.getAddTime());
        purchaseRequest.setUpdateTime(po.getUpdateTime());
        purchaseRequest.setCreatorId(po.getCreatorId());
        purchaseRequest.setModifyId(po.getModifyId());
        return purchaseRequest;
    }
}
