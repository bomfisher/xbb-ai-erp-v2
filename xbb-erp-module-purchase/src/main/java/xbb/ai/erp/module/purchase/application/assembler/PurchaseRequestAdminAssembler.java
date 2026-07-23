package xbb.ai.erp.module.purchase.application.assembler;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSaveItemVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;

public final class PurchaseRequestAdminAssembler {

    private PurchaseRequestAdminAssembler() {
    }

    public static PurchaseRequestSaveItemVO buildEmptySaveItemVO() {
        return new PurchaseRequestSaveItemVO();
    }

    public static PurchaseRequest toPurchaseRequest(PurchaseRequestSaveDTO dto) {
        PurchaseRequest purchaseRequest = new PurchaseRequest();
        PurchaseRequestMainDTO main = dto.getMain();
        if (main != null) {
            purchaseRequest.setId(main.getId());
            purchaseRequest.setCorpid(main.getCorpid());
            purchaseRequest.setPurchaseOrgId(main.getPurchaseOrgId());
            purchaseRequest.setRequestNo(main.getRequestNo());
            purchaseRequest.setRequestDeptId(main.getRequestDeptId());
            purchaseRequest.setApplicantId(main.getApplicantId());
            purchaseRequest.setSourceType(main.getSourceType());
            purchaseRequest.setSourceNo(main.getSourceNo());
            purchaseRequest.setSuggestedVendorId(main.getSuggestedVendorId());
            purchaseRequest.setSuggestedDeliveryDate(main.getSuggestedDeliveryDate());
            purchaseRequest.setBizStatus(main.getBizStatus());
            purchaseRequest.setApprovalStatus(main.getApprovalStatus());
            purchaseRequest.setGrossAmount(main.getGrossAmount());
            purchaseRequest.setNetAmount(main.getNetAmount());
            purchaseRequest.setTaxAmount(main.getTaxAmount());
            purchaseRequest.setVersion(main.getVersion());
            purchaseRequest.setRemark(main.getRemark());
            purchaseRequest.setDeleted(main.getDeleted());
            purchaseRequest.setAddTime(main.getAddTime());
            purchaseRequest.setUpdateTime(main.getUpdateTime());
            purchaseRequest.setCreatorId(main.getCreatorId());
            purchaseRequest.setModifyId(main.getModifyId());
        }
        purchaseRequest.setCorpid(dto.getCorpid());
        return purchaseRequest;
    }

    public static PurchaseRequestListItemVO toListItemVO(PurchaseRequest purchaseRequest) {
        PurchaseRequestListItemVO vo = new PurchaseRequestListItemVO();
        vo.setId(purchaseRequest.getId());
        vo.setPurchaseOrgId(purchaseRequest.getPurchaseOrgId());
        vo.setRequestNo(purchaseRequest.getRequestNo());
        vo.setSourceType(purchaseRequest.getSourceType());
        vo.setBizStatus(purchaseRequest.getBizStatus());
        vo.setApprovalStatus(purchaseRequest.getApprovalStatus());
        vo.setGrossAmount(purchaseRequest.getGrossAmount());
        vo.setNetAmount(purchaseRequest.getNetAmount());
        vo.setTaxAmount(purchaseRequest.getTaxAmount());
        vo.setAddTime(purchaseRequest.getAddTime());
        vo.setUpdateTime(purchaseRequest.getUpdateTime());
        return vo;
    }

    public static PurchaseRequestSaveItemVO toSaveItemVO(PurchaseRequest purchaseRequest) {
        PurchaseRequestSaveItemVO vo = new PurchaseRequestSaveItemVO();
        if (purchaseRequest == null) {
            return vo;
        }
        PurchaseRequestMainDTO main = new PurchaseRequestMainDTO();
        main.setId(purchaseRequest.getId());
        main.setCorpid(purchaseRequest.getCorpid());
        main.setPurchaseOrgId(purchaseRequest.getPurchaseOrgId());
        main.setRequestNo(purchaseRequest.getRequestNo());
        main.setRequestDeptId(purchaseRequest.getRequestDeptId());
        main.setApplicantId(purchaseRequest.getApplicantId());
        main.setSourceType(purchaseRequest.getSourceType());
        main.setSourceNo(purchaseRequest.getSourceNo());
        main.setSuggestedVendorId(purchaseRequest.getSuggestedVendorId());
        main.setSuggestedDeliveryDate(purchaseRequest.getSuggestedDeliveryDate());
        main.setBizStatus(purchaseRequest.getBizStatus());
        main.setApprovalStatus(purchaseRequest.getApprovalStatus());
        main.setGrossAmount(purchaseRequest.getGrossAmount());
        main.setNetAmount(purchaseRequest.getNetAmount());
        main.setTaxAmount(purchaseRequest.getTaxAmount());
        main.setVersion(purchaseRequest.getVersion());
        main.setRemark(purchaseRequest.getRemark());
        main.setDeleted(purchaseRequest.getDeleted());
        main.setAddTime(purchaseRequest.getAddTime());
        main.setUpdateTime(purchaseRequest.getUpdateTime());
        main.setCreatorId(purchaseRequest.getCreatorId());
        main.setModifyId(purchaseRequest.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static PurchaseRequestDetailVO toDetailVO(PurchaseRequestSaveItemVO saveItemVO) {
        PurchaseRequestDetailVO detailVO = new PurchaseRequestDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
