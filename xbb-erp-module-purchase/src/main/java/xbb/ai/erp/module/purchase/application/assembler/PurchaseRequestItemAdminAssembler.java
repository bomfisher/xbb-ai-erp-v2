package xbb.ai.erp.module.purchase.application.assembler;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestItemSaveItemVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem;

public final class PurchaseRequestItemAdminAssembler {

    private PurchaseRequestItemAdminAssembler() {
    }

    public static PurchaseRequestItemSaveItemVO buildEmptySaveItemVO() {
        return new PurchaseRequestItemSaveItemVO();
    }

    public static PurchaseRequestItem toPurchaseRequestItem(PurchaseRequestItemSaveDTO dto) {
        PurchaseRequestItem purchaseRequestItem = new PurchaseRequestItem();
        PurchaseRequestItemMainDTO main = dto.getMain();
        if (main != null) {
            purchaseRequestItem.setId(main.getId());
            purchaseRequestItem.setCorpid(main.getCorpid());
            purchaseRequestItem.setRequestId(main.getRequestId());
            purchaseRequestItem.setLineNo(main.getLineNo());
            purchaseRequestItem.setSkuId(main.getSkuId());
            purchaseRequestItem.setSkuCodeSnapshot(main.getSkuCodeSnapshot());
            purchaseRequestItem.setSkuNameSnapshot(main.getSkuNameSnapshot());
            purchaseRequestItem.setSpecSnapshot(main.getSpecSnapshot());
            purchaseRequestItem.setPurchaseUnitId(main.getPurchaseUnitId());
            purchaseRequestItem.setRequestQty(main.getRequestQty());
            purchaseRequestItem.setReservedQty(main.getReservedQty());
            purchaseRequestItem.setExecutedQty(main.getExecutedQty());
            purchaseRequestItem.setClosedQty(main.getClosedQty());
            purchaseRequestItem.setSuggestedVendorId(main.getSuggestedVendorId());
            purchaseRequestItem.setSuggestedDeliveryDate(main.getSuggestedDeliveryDate());
            purchaseRequestItem.setVersion(main.getVersion());
            purchaseRequestItem.setDeleted(main.getDeleted());
            purchaseRequestItem.setAddTime(main.getAddTime());
            purchaseRequestItem.setUpdateTime(main.getUpdateTime());
            purchaseRequestItem.setCreatorId(main.getCreatorId());
            purchaseRequestItem.setModifyId(main.getModifyId());
        }
        purchaseRequestItem.setCorpid(dto.getCorpid());
        return purchaseRequestItem;
    }

    public static PurchaseRequestItemListItemVO toListItemVO(PurchaseRequestItem purchaseRequestItem) {
        PurchaseRequestItemListItemVO vo = new PurchaseRequestItemListItemVO();
        vo.setId(purchaseRequestItem.getId());
        vo.setRequestId(purchaseRequestItem.getRequestId());
        vo.setLineNo(purchaseRequestItem.getLineNo());
        vo.setSkuId(purchaseRequestItem.getSkuId());
        vo.setSkuCodeSnapshot(purchaseRequestItem.getSkuCodeSnapshot());
        vo.setSkuNameSnapshot(purchaseRequestItem.getSkuNameSnapshot());
        vo.setRequestQty(purchaseRequestItem.getRequestQty());
        vo.setReservedQty(purchaseRequestItem.getReservedQty());
        vo.setExecutedQty(purchaseRequestItem.getExecutedQty());
        vo.setClosedQty(purchaseRequestItem.getClosedQty());
        vo.setAddTime(purchaseRequestItem.getAddTime());
        vo.setUpdateTime(purchaseRequestItem.getUpdateTime());
        return vo;
    }

    public static PurchaseRequestItemSaveItemVO toSaveItemVO(PurchaseRequestItem purchaseRequestItem) {
        PurchaseRequestItemSaveItemVO vo = new PurchaseRequestItemSaveItemVO();
        if (purchaseRequestItem == null) {
            return vo;
        }
        PurchaseRequestItemMainDTO main = new PurchaseRequestItemMainDTO();
        main.setId(purchaseRequestItem.getId());
        main.setCorpid(purchaseRequestItem.getCorpid());
        main.setRequestId(purchaseRequestItem.getRequestId());
        main.setLineNo(purchaseRequestItem.getLineNo());
        main.setSkuId(purchaseRequestItem.getSkuId());
        main.setSkuCodeSnapshot(purchaseRequestItem.getSkuCodeSnapshot());
        main.setSkuNameSnapshot(purchaseRequestItem.getSkuNameSnapshot());
        main.setSpecSnapshot(purchaseRequestItem.getSpecSnapshot());
        main.setPurchaseUnitId(purchaseRequestItem.getPurchaseUnitId());
        main.setRequestQty(purchaseRequestItem.getRequestQty());
        main.setReservedQty(purchaseRequestItem.getReservedQty());
        main.setExecutedQty(purchaseRequestItem.getExecutedQty());
        main.setClosedQty(purchaseRequestItem.getClosedQty());
        main.setSuggestedVendorId(purchaseRequestItem.getSuggestedVendorId());
        main.setSuggestedDeliveryDate(purchaseRequestItem.getSuggestedDeliveryDate());
        main.setVersion(purchaseRequestItem.getVersion());
        main.setDeleted(purchaseRequestItem.getDeleted());
        main.setAddTime(purchaseRequestItem.getAddTime());
        main.setUpdateTime(purchaseRequestItem.getUpdateTime());
        main.setCreatorId(purchaseRequestItem.getCreatorId());
        main.setModifyId(purchaseRequestItem.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static PurchaseRequestItemDetailVO toDetailVO(PurchaseRequestItemSaveItemVO saveItemVO) {
        PurchaseRequestItemDetailVO detailVO = new PurchaseRequestItemDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
