package xbb.ai.erp.module.purchase.application.assembler;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;

public final class PurchaseOrderAdminAssembler {

    private PurchaseOrderAdminAssembler() {
    }

    public static PurchaseOrderSaveItemVO buildEmptySaveItemVO() {
        return new PurchaseOrderSaveItemVO();
    }

    public static PurchaseOrder toPurchaseOrder(PurchaseOrderSaveDTO dto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        PurchaseOrderMainDTO main = dto.getMain();
        if (main != null) {
            purchaseOrder.setId(main.getId());
            purchaseOrder.setCorpid(main.getCorpid());
            purchaseOrder.setOrderNo(main.getOrderNo());
            purchaseOrder.setSupplierId(main.getSupplierId());
            purchaseOrder.setSupplierName(main.getSupplierName());
            purchaseOrder.setOrderDate(main.getOrderDate());
            purchaseOrder.setExpectedDate(main.getExpectedDate());
            purchaseOrder.setTotalAmount(main.getTotalAmount());
            purchaseOrder.setStatus(main.getStatus());
            purchaseOrder.setRemark(main.getRemark());
            purchaseOrder.setCreatorId(main.getCreatorId());
            purchaseOrder.setModifyId(main.getModifyId());
        }
        purchaseOrder.setCorpid(dto.getCorpid());
        return purchaseOrder;
    }

    public static PurchaseOrderListItemVO toListItemVO(PurchaseOrder purchaseOrder) {
        PurchaseOrderListItemVO vo = new PurchaseOrderListItemVO();
        vo.setId(purchaseOrder.getId());
        vo.setOrderNo(purchaseOrder.getOrderNo());
        vo.setSupplierId(purchaseOrder.getSupplierId());
        vo.setSupplierName(purchaseOrder.getSupplierName());
        vo.setOrderDate(purchaseOrder.getOrderDate());
        vo.setExpectedDate(purchaseOrder.getExpectedDate());
        vo.setTotalAmount(purchaseOrder.getTotalAmount());
        vo.setStatus(purchaseOrder.getStatus());
        vo.setRemark(purchaseOrder.getRemark());
        vo.setCreatorId(purchaseOrder.getCreatorId());
        vo.setModifyId(purchaseOrder.getModifyId());
        return vo;
    }

    public static PurchaseOrderSaveItemVO toSaveItemVO(PurchaseOrder purchaseOrder) {
        PurchaseOrderSaveItemVO vo = new PurchaseOrderSaveItemVO();
        if (purchaseOrder == null) {
            return vo;
        }
        PurchaseOrderMainDTO main = new PurchaseOrderMainDTO();
        main.setId(purchaseOrder.getId());
        main.setCorpid(purchaseOrder.getCorpid());
        main.setOrderNo(purchaseOrder.getOrderNo());
        main.setSupplierId(purchaseOrder.getSupplierId());
        main.setSupplierName(purchaseOrder.getSupplierName());
        main.setOrderDate(purchaseOrder.getOrderDate());
        main.setExpectedDate(purchaseOrder.getExpectedDate());
        main.setTotalAmount(purchaseOrder.getTotalAmount());
        main.setStatus(purchaseOrder.getStatus());
        main.setRemark(purchaseOrder.getRemark());
        main.setCreatorId(purchaseOrder.getCreatorId());
        main.setModifyId(purchaseOrder.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static PurchaseOrderDetailVO toDetailVO(PurchaseOrderSaveItemVO saveItemVO) {
        PurchaseOrderDetailVO detailVO = new PurchaseOrderDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
