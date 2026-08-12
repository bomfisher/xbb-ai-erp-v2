package xbb.ai.erp.module.purchase.application.assembler;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;

public final class PurchaseInboundAdminAssembler {

    private PurchaseInboundAdminAssembler() {
    }

    public static PurchaseInboundSaveItemVO buildEmptySaveItemVO() {
        return new PurchaseInboundSaveItemVO();
    }

    public static PurchaseInbound toPurchaseInbound(PurchaseInboundSaveDTO dto) {
        PurchaseInbound purchaseInbound = new PurchaseInbound();
        PurchaseInboundMainDTO main = dto.getMain();
        if (main != null) {
            purchaseInbound.setId(main.getId());
            purchaseInbound.setCorpid(main.getCorpid());
            purchaseInbound.setInboundNo(main.getInboundNo());
            purchaseInbound.setPurchaseOrderId(main.getPurchaseOrderId());
            purchaseInbound.setSupplierId(main.getSupplierId());
            purchaseInbound.setSupplierName(main.getSupplierName());
            purchaseInbound.setWarehouseId(main.getWarehouseId());
            purchaseInbound.setInboundDate(main.getInboundDate());
            purchaseInbound.setTotalAmount(main.getTotalAmount());
            purchaseInbound.setStatus(main.getStatus());
            purchaseInbound.setRemark(main.getRemark());
            purchaseInbound.setCreatorId(main.getCreatorId());
            purchaseInbound.setModifyId(main.getModifyId());
        }
        purchaseInbound.setCorpid(dto.getCorpid());
        return purchaseInbound;
    }

    public static PurchaseInboundListItemVO toListItemVO(PurchaseInbound purchaseInbound) {
        PurchaseInboundListItemVO vo = new PurchaseInboundListItemVO();
        vo.setId(purchaseInbound.getId());
        vo.setInboundNo(purchaseInbound.getInboundNo());
        vo.setPurchaseOrderId(purchaseInbound.getPurchaseOrderId());
        vo.setSupplierId(purchaseInbound.getSupplierId());
        vo.setSupplierName(purchaseInbound.getSupplierName());
        vo.setWarehouseId(purchaseInbound.getWarehouseId());
        vo.setInboundDate(purchaseInbound.getInboundDate());
        vo.setTotalAmount(purchaseInbound.getTotalAmount());
        vo.setStatus(purchaseInbound.getStatus());
        vo.setRemark(purchaseInbound.getRemark());
        vo.setCreatorId(purchaseInbound.getCreatorId());
        vo.setModifyId(purchaseInbound.getModifyId());
        return vo;
    }

    public static PurchaseInboundSaveItemVO toSaveItemVO(PurchaseInbound purchaseInbound) {
        PurchaseInboundSaveItemVO vo = new PurchaseInboundSaveItemVO();
        if (purchaseInbound == null) {
            return vo;
        }
        PurchaseInboundMainDTO main = new PurchaseInboundMainDTO();
        main.setId(purchaseInbound.getId());
        main.setCorpid(purchaseInbound.getCorpid());
        main.setInboundNo(purchaseInbound.getInboundNo());
        main.setPurchaseOrderId(purchaseInbound.getPurchaseOrderId());
        main.setSupplierId(purchaseInbound.getSupplierId());
        main.setSupplierName(purchaseInbound.getSupplierName());
        main.setWarehouseId(purchaseInbound.getWarehouseId());
        main.setInboundDate(purchaseInbound.getInboundDate());
        main.setTotalAmount(purchaseInbound.getTotalAmount());
        main.setStatus(purchaseInbound.getStatus());
        main.setRemark(purchaseInbound.getRemark());
        main.setCreatorId(purchaseInbound.getCreatorId());
        main.setModifyId(purchaseInbound.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static PurchaseInboundDetailVO toDetailVO(PurchaseInboundSaveItemVO saveItemVO) {
        PurchaseInboundDetailVO detailVO = new PurchaseInboundDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
