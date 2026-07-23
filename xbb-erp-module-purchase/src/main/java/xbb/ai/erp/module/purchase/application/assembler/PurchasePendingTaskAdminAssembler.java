package xbb.ai.erp.module.purchase.application.assembler;

import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchasePendingTaskSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchasePendingTaskSaveItemVO;
import xbb.ai.erp.module.purchase.domain.model.PurchasePendingTask;

public final class PurchasePendingTaskAdminAssembler {

    private PurchasePendingTaskAdminAssembler() {
    }

    public static PurchasePendingTaskSaveItemVO buildEmptySaveItemVO() {
        return new PurchasePendingTaskSaveItemVO();
    }

    public static PurchasePendingTask toPurchasePendingTask(PurchasePendingTaskSaveDTO dto) {
        PurchasePendingTask purchasePendingTask = new PurchasePendingTask();
        PurchasePendingTaskMainDTO main = dto.getMain();
        if (main != null) {
            purchasePendingTask.setId(main.getId());
            purchasePendingTask.setCorpid(main.getCorpid());
            purchasePendingTask.setPurchaseOrgId(main.getPurchaseOrgId());
            purchasePendingTask.setTaskNo(main.getTaskNo());
            purchasePendingTask.setSourceType(main.getSourceType());
            purchasePendingTask.setSourceDocId(main.getSourceDocId());
            purchasePendingTask.setSourceLineId(main.getSourceLineId());
            purchasePendingTask.setSourceDocNo(main.getSourceDocNo());
            purchasePendingTask.setSkuId(main.getSkuId());
            purchasePendingTask.setSkuCodeSnapshot(main.getSkuCodeSnapshot());
            purchasePendingTask.setSkuNameSnapshot(main.getSkuNameSnapshot());
            purchasePendingTask.setNeedQty(main.getNeedQty());
            purchasePendingTask.setOccupiedQty(main.getOccupiedQty());
            purchasePendingTask.setGeneratedRequestQty(main.getGeneratedRequestQty());
            purchasePendingTask.setGeneratedOrderQty(main.getGeneratedOrderQty());
            purchasePendingTask.setClosedQty(main.getClosedQty());
            purchasePendingTask.setSuggestedVendorId(main.getSuggestedVendorId());
            purchasePendingTask.setSuggestedDeliveryDate(main.getSuggestedDeliveryDate());
            purchasePendingTask.setPriorityLevel(main.getPriorityLevel());
            purchasePendingTask.setTaskStatus(main.getTaskStatus());
            purchasePendingTask.setSalesLinkedFlag(main.getSalesLinkedFlag());
            purchasePendingTask.setVersion(main.getVersion());
            purchasePendingTask.setDeleted(main.getDeleted());
            purchasePendingTask.setAddTime(main.getAddTime());
            purchasePendingTask.setUpdateTime(main.getUpdateTime());
            purchasePendingTask.setCreatorId(main.getCreatorId());
            purchasePendingTask.setModifyId(main.getModifyId());
        }
        purchasePendingTask.setCorpid(dto.getCorpid());
        return purchasePendingTask;
    }

    public static PurchasePendingTaskListItemVO toListItemVO(PurchasePendingTask purchasePendingTask) {
        PurchasePendingTaskListItemVO vo = new PurchasePendingTaskListItemVO();
        vo.setId(purchasePendingTask.getId());
        vo.setPurchaseOrgId(purchasePendingTask.getPurchaseOrgId());
        vo.setTaskNo(purchasePendingTask.getTaskNo());
        vo.setSourceType(purchasePendingTask.getSourceType());
        vo.setSkuId(purchasePendingTask.getSkuId());
        vo.setSkuCodeSnapshot(purchasePendingTask.getSkuCodeSnapshot());
        vo.setSkuNameSnapshot(purchasePendingTask.getSkuNameSnapshot());
        vo.setNeedQty(purchasePendingTask.getNeedQty());
        vo.setOccupiedQty(purchasePendingTask.getOccupiedQty());
        vo.setGeneratedRequestQty(purchasePendingTask.getGeneratedRequestQty());
        vo.setGeneratedOrderQty(purchasePendingTask.getGeneratedOrderQty());
        vo.setClosedQty(purchasePendingTask.getClosedQty());
        vo.setPriorityLevel(purchasePendingTask.getPriorityLevel());
        vo.setTaskStatus(purchasePendingTask.getTaskStatus());
        vo.setSalesLinkedFlag(purchasePendingTask.getSalesLinkedFlag());
        vo.setAddTime(purchasePendingTask.getAddTime());
        vo.setUpdateTime(purchasePendingTask.getUpdateTime());
        return vo;
    }

    public static PurchasePendingTaskSaveItemVO toSaveItemVO(PurchasePendingTask purchasePendingTask) {
        PurchasePendingTaskSaveItemVO vo = new PurchasePendingTaskSaveItemVO();
        if (purchasePendingTask == null) {
            return vo;
        }
        PurchasePendingTaskMainDTO main = new PurchasePendingTaskMainDTO();
        main.setId(purchasePendingTask.getId());
        main.setCorpid(purchasePendingTask.getCorpid());
        main.setPurchaseOrgId(purchasePendingTask.getPurchaseOrgId());
        main.setTaskNo(purchasePendingTask.getTaskNo());
        main.setSourceType(purchasePendingTask.getSourceType());
        main.setSourceDocId(purchasePendingTask.getSourceDocId());
        main.setSourceLineId(purchasePendingTask.getSourceLineId());
        main.setSourceDocNo(purchasePendingTask.getSourceDocNo());
        main.setSkuId(purchasePendingTask.getSkuId());
        main.setSkuCodeSnapshot(purchasePendingTask.getSkuCodeSnapshot());
        main.setSkuNameSnapshot(purchasePendingTask.getSkuNameSnapshot());
        main.setNeedQty(purchasePendingTask.getNeedQty());
        main.setOccupiedQty(purchasePendingTask.getOccupiedQty());
        main.setGeneratedRequestQty(purchasePendingTask.getGeneratedRequestQty());
        main.setGeneratedOrderQty(purchasePendingTask.getGeneratedOrderQty());
        main.setClosedQty(purchasePendingTask.getClosedQty());
        main.setSuggestedVendorId(purchasePendingTask.getSuggestedVendorId());
        main.setSuggestedDeliveryDate(purchasePendingTask.getSuggestedDeliveryDate());
        main.setPriorityLevel(purchasePendingTask.getPriorityLevel());
        main.setTaskStatus(purchasePendingTask.getTaskStatus());
        main.setSalesLinkedFlag(purchasePendingTask.getSalesLinkedFlag());
        main.setVersion(purchasePendingTask.getVersion());
        main.setDeleted(purchasePendingTask.getDeleted());
        main.setAddTime(purchasePendingTask.getAddTime());
        main.setUpdateTime(purchasePendingTask.getUpdateTime());
        main.setCreatorId(purchasePendingTask.getCreatorId());
        main.setModifyId(purchasePendingTask.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static PurchasePendingTaskDetailVO toDetailVO(PurchasePendingTaskSaveItemVO saveItemVO) {
        PurchasePendingTaskDetailVO detailVO = new PurchasePendingTaskDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
