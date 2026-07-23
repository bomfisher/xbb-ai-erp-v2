package xbb.ai.erp.module.purchase.application.assembler;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseSourceRelationSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseSourceRelationSaveItemVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseSourceRelation;

public final class PurchaseSourceRelationAdminAssembler {

    private PurchaseSourceRelationAdminAssembler() {
    }

    public static PurchaseSourceRelationSaveItemVO buildEmptySaveItemVO() {
        return new PurchaseSourceRelationSaveItemVO();
    }

    public static PurchaseSourceRelation toPurchaseSourceRelation(PurchaseSourceRelationSaveDTO dto) {
        PurchaseSourceRelation purchaseSourceRelation = new PurchaseSourceRelation();
        PurchaseSourceRelationMainDTO main = dto.getMain();
        if (main != null) {
            purchaseSourceRelation.setId(main.getId());
            purchaseSourceRelation.setCorpid(main.getCorpid());
            purchaseSourceRelation.setSourceDocType(main.getSourceDocType());
            purchaseSourceRelation.setSourceDocId(main.getSourceDocId());
            purchaseSourceRelation.setSourceLineId(main.getSourceLineId());
            purchaseSourceRelation.setTargetDocType(main.getTargetDocType());
            purchaseSourceRelation.setTargetDocId(main.getTargetDocId());
            purchaseSourceRelation.setTargetLineId(main.getTargetLineId());
            purchaseSourceRelation.setSourceQty(main.getSourceQty());
            purchaseSourceRelation.setReservedQty(main.getReservedQty());
            purchaseSourceRelation.setExecutedQty(main.getExecutedQty());
            purchaseSourceRelation.setClosedQty(main.getClosedQty());
            purchaseSourceRelation.setReversedQty(main.getReversedQty());
            purchaseSourceRelation.setRelationStatus(main.getRelationStatus());
            purchaseSourceRelation.setVersion(main.getVersion());
            purchaseSourceRelation.setDeleted(main.getDeleted());
            purchaseSourceRelation.setAddTime(main.getAddTime());
            purchaseSourceRelation.setUpdateTime(main.getUpdateTime());
            purchaseSourceRelation.setCreatorId(main.getCreatorId());
            purchaseSourceRelation.setModifyId(main.getModifyId());
        }
        purchaseSourceRelation.setCorpid(dto.getCorpid());
        return purchaseSourceRelation;
    }

    public static PurchaseSourceRelationListItemVO toListItemVO(PurchaseSourceRelation purchaseSourceRelation) {
        PurchaseSourceRelationListItemVO vo = new PurchaseSourceRelationListItemVO();
        vo.setId(purchaseSourceRelation.getId());
        vo.setSourceDocType(purchaseSourceRelation.getSourceDocType());
        vo.setSourceDocId(purchaseSourceRelation.getSourceDocId());
        vo.setTargetDocType(purchaseSourceRelation.getTargetDocType());
        vo.setTargetDocId(purchaseSourceRelation.getTargetDocId());
        vo.setSourceQty(purchaseSourceRelation.getSourceQty());
        vo.setReservedQty(purchaseSourceRelation.getReservedQty());
        vo.setExecutedQty(purchaseSourceRelation.getExecutedQty());
        vo.setClosedQty(purchaseSourceRelation.getClosedQty());
        vo.setReversedQty(purchaseSourceRelation.getReversedQty());
        vo.setRelationStatus(purchaseSourceRelation.getRelationStatus());
        vo.setAddTime(purchaseSourceRelation.getAddTime());
        vo.setUpdateTime(purchaseSourceRelation.getUpdateTime());
        return vo;
    }

    public static PurchaseSourceRelationSaveItemVO toSaveItemVO(PurchaseSourceRelation purchaseSourceRelation) {
        PurchaseSourceRelationSaveItemVO vo = new PurchaseSourceRelationSaveItemVO();
        if (purchaseSourceRelation == null) {
            return vo;
        }
        PurchaseSourceRelationMainDTO main = new PurchaseSourceRelationMainDTO();
        main.setId(purchaseSourceRelation.getId());
        main.setCorpid(purchaseSourceRelation.getCorpid());
        main.setSourceDocType(purchaseSourceRelation.getSourceDocType());
        main.setSourceDocId(purchaseSourceRelation.getSourceDocId());
        main.setSourceLineId(purchaseSourceRelation.getSourceLineId());
        main.setTargetDocType(purchaseSourceRelation.getTargetDocType());
        main.setTargetDocId(purchaseSourceRelation.getTargetDocId());
        main.setTargetLineId(purchaseSourceRelation.getTargetLineId());
        main.setSourceQty(purchaseSourceRelation.getSourceQty());
        main.setReservedQty(purchaseSourceRelation.getReservedQty());
        main.setExecutedQty(purchaseSourceRelation.getExecutedQty());
        main.setClosedQty(purchaseSourceRelation.getClosedQty());
        main.setReversedQty(purchaseSourceRelation.getReversedQty());
        main.setRelationStatus(purchaseSourceRelation.getRelationStatus());
        main.setVersion(purchaseSourceRelation.getVersion());
        main.setDeleted(purchaseSourceRelation.getDeleted());
        main.setAddTime(purchaseSourceRelation.getAddTime());
        main.setUpdateTime(purchaseSourceRelation.getUpdateTime());
        main.setCreatorId(purchaseSourceRelation.getCreatorId());
        main.setModifyId(purchaseSourceRelation.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static PurchaseSourceRelationDetailVO toDetailVO(PurchaseSourceRelationSaveItemVO saveItemVO) {
        PurchaseSourceRelationDetailVO detailVO = new PurchaseSourceRelationDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
