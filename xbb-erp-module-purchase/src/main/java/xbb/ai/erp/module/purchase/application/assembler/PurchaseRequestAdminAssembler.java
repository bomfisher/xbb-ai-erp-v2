package xbb.ai.erp.module.purchase.application.assembler;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSectionStateDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestDraftMetaVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSaveItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseRequestSectionStateVO;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestDraftMetaPojo;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestSaveContextPojo;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestSaveExtPojo;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseRequestSectionStatePojo;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequest;
import xbb.ai.erp.module.purchase.domain.model.PurchaseRequestItem;

import java.util.List;

public final class PurchaseRequestAdminAssembler {

    private PurchaseRequestAdminAssembler() {
    }

    public static PurchaseRequestSaveItemVO buildEmptySaveItemVO() {
        PurchaseRequestSaveItemVO vo = new PurchaseRequestSaveItemVO();
        vo.setSectionState(defaultSectionState());
        return vo;
    }

    public static PurchaseRequestSaveContextPojo toDraftContext(PurchaseRequestDraftSaveDTO dto) {
        PurchaseRequestSaveContextPojo context = new PurchaseRequestSaveContextPojo();
        context.setCorpid(dto.getCorpid());
        context.setMain(dto.getMain());
        context.setExt(toSaveExtPojo(dto.getItems()));
        context.setSectionState(toSectionStatePojo(dto.getSectionState()));
        context.setDraftMeta(toDraftMetaPojo(dto.getDraftMeta()));
        context.setSubmitMode(0);
        return context;
    }

    public static PurchaseRequestSaveContextPojo toSubmitContext(PurchaseRequestSubmitSaveDTO dto) {
        PurchaseRequestSaveContextPojo context = new PurchaseRequestSaveContextPojo();
        context.setCorpid(dto.getCorpid());
        context.setMain(dto.getMain());
        context.setExt(toSaveExtPojo(dto.getItems()));
        context.setSectionState(toSectionStatePojo(dto.getSectionState()));
        context.setDraftMeta(toDraftMetaPojo(dto.getDraftMeta()));
        context.setSubmitMode(1);
        return context;
    }

    public static PurchaseRequestSaveDraftPojo toDraftPojo(PurchaseRequestDraftSaveDTO dto) {
        PurchaseRequestSaveDraftPojo draft = new PurchaseRequestSaveDraftPojo();
        draft.setCorpid(dto.getCorpid());
        draft.setDraftCode(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getDraftCode());
        draft.setDraftTitle(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getDraftTitle());
        draft.setUpdatedTime(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getUpdatedTime());
        draft.setMain(dto.getMain());
        draft.setItems(dto.getItems());
        draft.setSectionState(toSectionStatePojo(dto.getSectionState()));
        return draft;
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

    public static PurchaseRequestSaveItemVO toSaveItemVO(PurchaseRequest purchaseRequest, List<PurchaseRequestItem> items) {
        PurchaseRequestSaveItemVO vo = new PurchaseRequestSaveItemVO();
        if (purchaseRequest == null) {
            vo.setSectionState(defaultSectionState());
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
        vo.setItems(toItemDTOs(items));
        vo.setSectionState(defaultSectionState());
        return vo;
    }

    public static PurchaseRequestDetailVO toDetailVO(PurchaseRequestSaveItemVO saveItemVO) {
        PurchaseRequestDetailVO detailVO = new PurchaseRequestDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
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

    public static PurchaseRequestDraftListItemVO toDraftListItemVO(PurchaseRequestSaveDraftPojo pojo) {
        PurchaseRequestDraftListItemVO vo = new PurchaseRequestDraftListItemVO();
        vo.setDraftCode(pojo.getDraftCode());
        vo.setDraftTitle(pojo.getDraftTitle());
        if (pojo.getMain() != null) {
            vo.setRequestNo(pojo.getMain().getRequestNo());
            vo.setApplicantId(pojo.getMain().getApplicantId());
        }
        vo.setUpdatedTime(pojo.getUpdatedTime());
        return vo;
    }

    public static PurchaseRequestDraftDetailVO toDraftDetailVO(PurchaseRequestSaveDraftPojo pojo) {
        PurchaseRequestDraftDetailVO vo = new PurchaseRequestDraftDetailVO();
        if (pojo == null) {
            return vo;
        }
        if (pojo.getMain() != null) {
            vo.setMain(pojo.getMain());
        }
        vo.setItems(pojo.getItems() == null ? List.of() : pojo.getItems());
        if (pojo.getSectionState() != null) {
            vo.setSectionState(toSectionStateVO(pojo.getSectionState()));
        }
        vo.setDraftMeta(toDraftMetaVO(pojo));
        return vo;
    }

    public static PurchaseRequestItem toPurchaseRequestItem(String corpid, Long requestId, PurchaseRequestItemMainDTO item) {
        PurchaseRequestItem domain = new PurchaseRequestItem();
        domain.setId(item.getId());
        domain.setCorpid(corpid);
        domain.setRequestId(requestId);
        domain.setLineNo(item.getLineNo());
        domain.setSkuId(item.getSkuId());
        domain.setSkuCodeSnapshot(item.getSkuCodeSnapshot());
        domain.setSkuNameSnapshot(item.getSkuNameSnapshot());
        domain.setSpecSnapshot(item.getSpecSnapshot());
        domain.setPurchaseUnitId(item.getPurchaseUnitId());
        domain.setRequestQty(item.getRequestQty());
        domain.setReservedQty(item.getReservedQty());
        domain.setExecutedQty(item.getExecutedQty());
        domain.setClosedQty(item.getClosedQty());
        domain.setSuggestedVendorId(item.getSuggestedVendorId());
        domain.setSuggestedDeliveryDate(item.getSuggestedDeliveryDate());
        domain.setVersion(item.getVersion());
        domain.setDeleted(item.getDeleted());
        domain.setAddTime(item.getAddTime());
        domain.setUpdateTime(item.getUpdateTime());
        domain.setCreatorId(item.getCreatorId());
        domain.setModifyId(item.getModifyId());
        return domain;
    }

    private static List<PurchaseRequestItemMainDTO> toItemDTOs(List<PurchaseRequestItem> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream().map(item -> {
            PurchaseRequestItemMainDTO dto = new PurchaseRequestItemMainDTO();
            dto.setId(item.getId());
            dto.setCorpid(item.getCorpid());
            dto.setRequestId(item.getRequestId());
            dto.setLineNo(item.getLineNo());
            dto.setSkuId(item.getSkuId());
            dto.setSkuCodeSnapshot(item.getSkuCodeSnapshot());
            dto.setSkuNameSnapshot(item.getSkuNameSnapshot());
            dto.setSpecSnapshot(item.getSpecSnapshot());
            dto.setPurchaseUnitId(item.getPurchaseUnitId());
            dto.setRequestQty(item.getRequestQty());
            dto.setReservedQty(item.getReservedQty());
            dto.setExecutedQty(item.getExecutedQty());
            dto.setClosedQty(item.getClosedQty());
            dto.setSuggestedVendorId(item.getSuggestedVendorId());
            dto.setSuggestedDeliveryDate(item.getSuggestedDeliveryDate());
            dto.setVersion(item.getVersion());
            dto.setDeleted(item.getDeleted());
            dto.setAddTime(item.getAddTime());
            dto.setUpdateTime(item.getUpdateTime());
            dto.setCreatorId(item.getCreatorId());
            dto.setModifyId(item.getModifyId());
            return dto;
        }).toList();
    }

    private static PurchaseRequestSaveExtPojo toSaveExtPojo(List<PurchaseRequestItemMainDTO> items) {
        PurchaseRequestSaveExtPojo ext = new PurchaseRequestSaveExtPojo();
        ext.setItems(items == null ? List.of() : items);
        return ext;
    }

    private static PurchaseRequestSectionStatePojo toSectionStatePojo(PurchaseRequestSectionStateDTO dto) {
        PurchaseRequestSectionStatePojo pojo = new PurchaseRequestSectionStatePojo();
        if (dto != null) {
            pojo.setItems(dto.getItems());
        }
        return pojo;
    }

    private static PurchaseRequestDraftMetaPojo toDraftMetaPojo(xbb.ai.erp.module.purchase.admin.dto.PurchaseRequestDraftMetaDTO dto) {
        PurchaseRequestDraftMetaPojo pojo = new PurchaseRequestDraftMetaPojo();
        if (dto != null) {
            pojo.setDraftCode(dto.getDraftCode());
            pojo.setDraftTitle(dto.getDraftTitle());
            pojo.setUpdatedTime(dto.getUpdatedTime());
        }
        return pojo;
    }

    private static PurchaseRequestSectionStateVO toSectionStateVO(PurchaseRequestSectionStatePojo pojo) {
        PurchaseRequestSectionStateVO vo = new PurchaseRequestSectionStateVO();
        vo.setItems(pojo.getItems());
        return vo;
    }

    private static PurchaseRequestDraftMetaVO toDraftMetaVO(PurchaseRequestSaveDraftPojo pojo) {
        PurchaseRequestDraftMetaVO vo = new PurchaseRequestDraftMetaVO();
        vo.setDraftCode(pojo.getDraftCode());
        vo.setDraftTitle(pojo.getDraftTitle());
        vo.setUpdatedTime(pojo.getUpdatedTime());
        return vo;
    }

    private static PurchaseRequestSectionStateVO defaultSectionState() {
        PurchaseRequestSectionStateVO vo = new PurchaseRequestSectionStateVO();
        vo.setItems(1);
        return vo;
    }
}
