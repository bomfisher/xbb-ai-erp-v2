package xbb.ai.erp.module.purchase.application.assembler;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderItemSaveItemVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;

public final class PurchaseOrderItemAdminAssembler {

    private PurchaseOrderItemAdminAssembler() {
    }

    public static PurchaseOrderItemSaveItemVO buildEmptySaveItemVO() {
        return new PurchaseOrderItemSaveItemVO();
    }

    public static PurchaseOrderItem toPurchaseOrderItem(PurchaseOrderItemSaveDTO dto) {
        PurchaseOrderItem purchaseOrderItem = new PurchaseOrderItem();
        PurchaseOrderItemMainDTO main = dto.getMain();
        if (main != null) {
            purchaseOrderItem.setId(main.getId());
            purchaseOrderItem.setCorpid(main.getCorpid());
            purchaseOrderItem.setOrderId(main.getOrderId());
            purchaseOrderItem.setLineNo(main.getLineNo());
            purchaseOrderItem.setSkuId(main.getSkuId());
            purchaseOrderItem.setSkuCodeSnapshot(main.getSkuCodeSnapshot());
            purchaseOrderItem.setSkuNameSnapshot(main.getSkuNameSnapshot());
            purchaseOrderItem.setSpecSnapshot(main.getSpecSnapshot());
            purchaseOrderItem.setPurchaseUnitId(main.getPurchaseUnitId());
            purchaseOrderItem.setWarehouseId(main.getWarehouseId());
            purchaseOrderItem.setOrderQty(main.getOrderQty());
            purchaseOrderItem.setReceivedQty(main.getReceivedQty());
            purchaseOrderItem.setInboundedQty(main.getInboundedQty());
            purchaseOrderItem.setClosedQty(main.getClosedQty());
            purchaseOrderItem.setReturnedQty(main.getReturnedQty());
            purchaseOrderItem.setGrossPrice(main.getGrossPrice());
            purchaseOrderItem.setNetPrice(main.getNetPrice());
            purchaseOrderItem.setTaxRate(main.getTaxRate());
            purchaseOrderItem.setTaxAmount(main.getTaxAmount());
            purchaseOrderItem.setGrossAmount(main.getGrossAmount());
            purchaseOrderItem.setNetAmount(main.getNetAmount());
            purchaseOrderItem.setPayableAmount(main.getPayableAmount());
            purchaseOrderItem.setPaidAmount(main.getPaidAmount());
            purchaseOrderItem.setInvoicedAmount(main.getInvoicedAmount());
            purchaseOrderItem.setIsGift(main.getIsGift());
            purchaseOrderItem.setDeliveryPlanSnapshot(main.getDeliveryPlanSnapshot());
            purchaseOrderItem.setVersion(main.getVersion());
            purchaseOrderItem.setDeleted(main.getDeleted());
            purchaseOrderItem.setAddTime(main.getAddTime());
            purchaseOrderItem.setUpdateTime(main.getUpdateTime());
            purchaseOrderItem.setCreatorId(main.getCreatorId());
            purchaseOrderItem.setModifyId(main.getModifyId());
        }
        purchaseOrderItem.setCorpid(dto.getCorpid());
        return purchaseOrderItem;
    }

    public static PurchaseOrderItemListItemVO toListItemVO(PurchaseOrderItem purchaseOrderItem) {
        PurchaseOrderItemListItemVO vo = new PurchaseOrderItemListItemVO();
        vo.setId(purchaseOrderItem.getId());
        vo.setOrderId(purchaseOrderItem.getOrderId());
        vo.setLineNo(purchaseOrderItem.getLineNo());
        vo.setSkuId(purchaseOrderItem.getSkuId());
        vo.setSkuCodeSnapshot(purchaseOrderItem.getSkuCodeSnapshot());
        vo.setSkuNameSnapshot(purchaseOrderItem.getSkuNameSnapshot());
        vo.setOrderQty(purchaseOrderItem.getOrderQty());
        vo.setReceivedQty(purchaseOrderItem.getReceivedQty());
        vo.setInboundedQty(purchaseOrderItem.getInboundedQty());
        vo.setClosedQty(purchaseOrderItem.getClosedQty());
        vo.setReturnedQty(purchaseOrderItem.getReturnedQty());
        vo.setGrossPrice(purchaseOrderItem.getGrossPrice());
        vo.setNetPrice(purchaseOrderItem.getNetPrice());
        vo.setTaxRate(purchaseOrderItem.getTaxRate());
        vo.setTaxAmount(purchaseOrderItem.getTaxAmount());
        vo.setGrossAmount(purchaseOrderItem.getGrossAmount());
        vo.setNetAmount(purchaseOrderItem.getNetAmount());
        vo.setAddTime(purchaseOrderItem.getAddTime());
        vo.setUpdateTime(purchaseOrderItem.getUpdateTime());
        return vo;
    }

    public static PurchaseOrderItemSaveItemVO toSaveItemVO(PurchaseOrderItem purchaseOrderItem) {
        PurchaseOrderItemSaveItemVO vo = new PurchaseOrderItemSaveItemVO();
        if (purchaseOrderItem == null) {
            return vo;
        }
        PurchaseOrderItemMainDTO main = new PurchaseOrderItemMainDTO();
        main.setId(purchaseOrderItem.getId());
        main.setCorpid(purchaseOrderItem.getCorpid());
        main.setOrderId(purchaseOrderItem.getOrderId());
        main.setLineNo(purchaseOrderItem.getLineNo());
        main.setSkuId(purchaseOrderItem.getSkuId());
        main.setSkuCodeSnapshot(purchaseOrderItem.getSkuCodeSnapshot());
        main.setSkuNameSnapshot(purchaseOrderItem.getSkuNameSnapshot());
        main.setSpecSnapshot(purchaseOrderItem.getSpecSnapshot());
        main.setPurchaseUnitId(purchaseOrderItem.getPurchaseUnitId());
        main.setWarehouseId(purchaseOrderItem.getWarehouseId());
        main.setOrderQty(purchaseOrderItem.getOrderQty());
        main.setReceivedQty(purchaseOrderItem.getReceivedQty());
        main.setInboundedQty(purchaseOrderItem.getInboundedQty());
        main.setClosedQty(purchaseOrderItem.getClosedQty());
        main.setReturnedQty(purchaseOrderItem.getReturnedQty());
        main.setGrossPrice(purchaseOrderItem.getGrossPrice());
        main.setNetPrice(purchaseOrderItem.getNetPrice());
        main.setTaxRate(purchaseOrderItem.getTaxRate());
        main.setTaxAmount(purchaseOrderItem.getTaxAmount());
        main.setGrossAmount(purchaseOrderItem.getGrossAmount());
        main.setNetAmount(purchaseOrderItem.getNetAmount());
        main.setPayableAmount(purchaseOrderItem.getPayableAmount());
        main.setPaidAmount(purchaseOrderItem.getPaidAmount());
        main.setInvoicedAmount(purchaseOrderItem.getInvoicedAmount());
        main.setIsGift(purchaseOrderItem.getIsGift());
        main.setDeliveryPlanSnapshot(purchaseOrderItem.getDeliveryPlanSnapshot());
        main.setVersion(purchaseOrderItem.getVersion());
        main.setDeleted(purchaseOrderItem.getDeleted());
        main.setAddTime(purchaseOrderItem.getAddTime());
        main.setUpdateTime(purchaseOrderItem.getUpdateTime());
        main.setCreatorId(purchaseOrderItem.getCreatorId());
        main.setModifyId(purchaseOrderItem.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static PurchaseOrderItemDetailVO toDetailVO(PurchaseOrderItemSaveItemVO saveItemVO) {
        PurchaseOrderItemDetailVO detailVO = new PurchaseOrderItemDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
