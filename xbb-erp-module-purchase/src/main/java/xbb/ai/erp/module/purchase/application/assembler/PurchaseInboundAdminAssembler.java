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
            purchaseInbound.setPurchaseOrgId(main.getPurchaseOrgId());
            purchaseInbound.setInboundNo(main.getInboundNo());
            purchaseInbound.setSourceDocType(main.getSourceDocType());
            purchaseInbound.setSourceDocId(main.getSourceDocId());
            purchaseInbound.setVendorId(main.getVendorId());
            purchaseInbound.setWarehouseId(main.getWarehouseId());
            purchaseInbound.setActualInboundTime(main.getActualInboundTime());
            purchaseInbound.setBizStatus(main.getBizStatus());
            purchaseInbound.setApprovalStatus(main.getApprovalStatus());
            purchaseInbound.setExecutionStatus(main.getExecutionStatus());
            purchaseInbound.setGrossAmount(main.getGrossAmount());
            purchaseInbound.setNetAmount(main.getNetAmount());
            purchaseInbound.setTaxAmount(main.getTaxAmount());
            purchaseInbound.setInventoryCostAmount(main.getInventoryCostAmount());
            purchaseInbound.setInventoryFlowNo(main.getInventoryFlowNo());
            purchaseInbound.setPayableTriggerStatus(main.getPayableTriggerStatus());
            purchaseInbound.setPayableNo(main.getPayableNo());
            purchaseInbound.setInvoiceSourceStatus(main.getInvoiceSourceStatus());
            purchaseInbound.setPeriodLockedFlag(main.getPeriodLockedFlag());
            purchaseInbound.setVersion(main.getVersion());
            purchaseInbound.setRemark(main.getRemark());
            purchaseInbound.setDeleted(main.getDeleted());
            purchaseInbound.setAddTime(main.getAddTime());
            purchaseInbound.setUpdateTime(main.getUpdateTime());
            purchaseInbound.setCreatorId(main.getCreatorId());
            purchaseInbound.setModifyId(main.getModifyId());
        }
        purchaseInbound.setCorpid(dto.getCorpid());
        return purchaseInbound;
    }

    public static PurchaseInboundListItemVO toListItemVO(PurchaseInbound purchaseInbound) {
        PurchaseInboundListItemVO vo = new PurchaseInboundListItemVO();
        vo.setId(purchaseInbound.getId());
        vo.setPurchaseOrgId(purchaseInbound.getPurchaseOrgId());
        vo.setInboundNo(purchaseInbound.getInboundNo());
        vo.setSourceDocType(purchaseInbound.getSourceDocType());
        vo.setVendorId(purchaseInbound.getVendorId());
        vo.setWarehouseId(purchaseInbound.getWarehouseId());
        vo.setActualInboundTime(purchaseInbound.getActualInboundTime());
        vo.setBizStatus(purchaseInbound.getBizStatus());
        vo.setExecutionStatus(purchaseInbound.getExecutionStatus());
        vo.setGrossAmount(purchaseInbound.getGrossAmount());
        vo.setNetAmount(purchaseInbound.getNetAmount());
        vo.setPayableTriggerStatus(purchaseInbound.getPayableTriggerStatus());
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
        main.setPurchaseOrgId(purchaseInbound.getPurchaseOrgId());
        main.setInboundNo(purchaseInbound.getInboundNo());
        main.setSourceDocType(purchaseInbound.getSourceDocType());
        main.setSourceDocId(purchaseInbound.getSourceDocId());
        main.setVendorId(purchaseInbound.getVendorId());
        main.setWarehouseId(purchaseInbound.getWarehouseId());
        main.setActualInboundTime(purchaseInbound.getActualInboundTime());
        main.setBizStatus(purchaseInbound.getBizStatus());
        main.setApprovalStatus(purchaseInbound.getApprovalStatus());
        main.setExecutionStatus(purchaseInbound.getExecutionStatus());
        main.setGrossAmount(purchaseInbound.getGrossAmount());
        main.setNetAmount(purchaseInbound.getNetAmount());
        main.setTaxAmount(purchaseInbound.getTaxAmount());
        main.setInventoryCostAmount(purchaseInbound.getInventoryCostAmount());
        main.setInventoryFlowNo(purchaseInbound.getInventoryFlowNo());
        main.setPayableTriggerStatus(purchaseInbound.getPayableTriggerStatus());
        main.setPayableNo(purchaseInbound.getPayableNo());
        main.setInvoiceSourceStatus(purchaseInbound.getInvoiceSourceStatus());
        main.setPeriodLockedFlag(purchaseInbound.getPeriodLockedFlag());
        main.setVersion(purchaseInbound.getVersion());
        main.setRemark(purchaseInbound.getRemark());
        main.setDeleted(purchaseInbound.getDeleted());
        main.setAddTime(purchaseInbound.getAddTime());
        main.setUpdateTime(purchaseInbound.getUpdateTime());
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
