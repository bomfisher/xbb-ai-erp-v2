package xbb.ai.erp.module.purchase.application.assembler;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSectionStateDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSubmitSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDraftMetaVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSectionStateVO;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderDraftMetaPojo;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveContextPojo;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveDraftPojo;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSaveExtPojo;
import xbb.ai.erp.module.purchase.application.pojo.PurchaseOrderSectionStatePojo;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;

import java.util.List;

public final class PurchaseOrderAdminAssembler {

    private PurchaseOrderAdminAssembler() {
    }

    public static PurchaseOrderSaveItemVO buildEmptySaveItemVO() {
        PurchaseOrderSaveItemVO vo = new PurchaseOrderSaveItemVO();
        vo.setSectionState(defaultSectionState());
        return vo;
    }

    public static PurchaseOrderSaveContextPojo toDraftContext(PurchaseOrderDraftSaveDTO dto) {
        PurchaseOrderSaveContextPojo context = new PurchaseOrderSaveContextPojo();
        context.setCorpid(dto.getCorpid());
        context.setMain(dto.getMain());
        context.setExt(toSaveExtPojo(dto.getItems()));
        context.setSectionState(toSectionStatePojo(dto.getSectionState()));
        context.setDraftMeta(toDraftMetaPojo(dto.getDraftMeta()));
        context.setSubmitMode(0);
        return context;
    }

    public static PurchaseOrderSaveContextPojo toSubmitContext(PurchaseOrderSubmitSaveDTO dto) {
        PurchaseOrderSaveContextPojo context = new PurchaseOrderSaveContextPojo();
        context.setCorpid(dto.getCorpid());
        context.setMain(dto.getMain());
        context.setExt(toSaveExtPojo(dto.getItems()));
        context.setSectionState(toSectionStatePojo(dto.getSectionState()));
        context.setDraftMeta(toDraftMetaPojo(dto.getDraftMeta()));
        context.setSubmitMode(1);
        return context;
    }

    public static PurchaseOrderSaveDraftPojo toDraftPojo(PurchaseOrderDraftSaveDTO dto) {
        PurchaseOrderSaveDraftPojo draft = new PurchaseOrderSaveDraftPojo();
        draft.setCorpid(dto.getCorpid());
        draft.setDraftCode(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getDraftCode());
        draft.setDraftTitle(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getDraftTitle());
        draft.setUpdatedTime(dto.getDraftMeta() == null ? null : dto.getDraftMeta().getUpdatedTime());
        draft.setMain(dto.getMain());
        draft.setItems(dto.getItems());
        draft.setSectionState(toSectionStatePojo(dto.getSectionState()));
        return draft;
    }

    public static PurchaseOrder toPurchaseOrder(PurchaseOrderSaveDTO dto) {
        PurchaseOrder purchaseOrder = new PurchaseOrder();
        PurchaseOrderMainDTO main = dto.getMain();
        if (main != null) {
            purchaseOrder.setId(main.getId());
            purchaseOrder.setCorpid(main.getCorpid());
            purchaseOrder.setPurchaseOrgId(main.getPurchaseOrgId());
            purchaseOrder.setOrderNo(main.getOrderNo());
            purchaseOrder.setVendorId(main.getVendorId());
            purchaseOrder.setVendorNameSnapshot(main.getVendorNameSnapshot());
            purchaseOrder.setPurchaserId(main.getPurchaserId());
            purchaseOrder.setPurchaserNameSnapshot(main.getPurchaserNameSnapshot());
            purchaseOrder.setWarehouseId(main.getWarehouseId());
            purchaseOrder.setWarehouseNameSnapshot(main.getWarehouseNameSnapshot());
            purchaseOrder.setSettlementMethodId(main.getSettlementMethodId());
            purchaseOrder.setSettlementMethodSnapshot(main.getSettlementMethodSnapshot());
            purchaseOrder.setPaymentTermSnapshot(main.getPaymentTermSnapshot());
            purchaseOrder.setCurrencyCode(main.getCurrencyCode());
            purchaseOrder.setDeliveryDate(main.getDeliveryDate());
            purchaseOrder.setSourceType(main.getSourceType());
            purchaseOrder.setSourceNo(main.getSourceNo());
            purchaseOrder.setSalesLinkedFlag(main.getSalesLinkedFlag());
            purchaseOrder.setBizStatus(main.getBizStatus());
            purchaseOrder.setApprovalStatus(main.getApprovalStatus());
            purchaseOrder.setExecutionStatus(main.getExecutionStatus());
            purchaseOrder.setReceiptStatus(main.getReceiptStatus());
            purchaseOrder.setInboundStatus(main.getInboundStatus());
            purchaseOrder.setPayableStatus(main.getPayableStatus());
            purchaseOrder.setInvoiceStatus(main.getInvoiceStatus());
            purchaseOrder.setPaymentStatus(main.getPaymentStatus());
            purchaseOrder.setGrossAmount(main.getGrossAmount());
            purchaseOrder.setNetAmount(main.getNetAmount());
            purchaseOrder.setTaxAmount(main.getTaxAmount());
            purchaseOrder.setInboundedQtySummary(main.getInboundedQtySummary());
            purchaseOrder.setUninboundedQtySummary(main.getUninboundedQtySummary());
            purchaseOrder.setClosedQtySummary(main.getClosedQtySummary());
            purchaseOrder.setPayableAmountSummary(main.getPayableAmountSummary());
            purchaseOrder.setPaidAmountSummary(main.getPaidAmountSummary());
            purchaseOrder.setInvoicedAmountSummary(main.getInvoicedAmountSummary());
            purchaseOrder.setLastInboundTime(main.getLastInboundTime());
            purchaseOrder.setLastPayableTime(main.getLastPayableTime());
            purchaseOrder.setPeriodLockedFlag(main.getPeriodLockedFlag());
            purchaseOrder.setVersion(main.getVersion());
            purchaseOrder.setRemark(main.getRemark());
            purchaseOrder.setDeleted(main.getDeleted());
            purchaseOrder.setAddTime(main.getAddTime());
            purchaseOrder.setUpdateTime(main.getUpdateTime());
            purchaseOrder.setCreatorId(main.getCreatorId());
            purchaseOrder.setModifyId(main.getModifyId());
        }
        purchaseOrder.setCorpid(dto.getCorpid());
        return purchaseOrder;
    }

    public static PurchaseOrderListItemVO toListItemVO(PurchaseOrder purchaseOrder) {
        PurchaseOrderListItemVO vo = new PurchaseOrderListItemVO();
        vo.setId(purchaseOrder.getId());
        vo.setPurchaseOrgId(purchaseOrder.getPurchaseOrgId());
        vo.setOrderNo(purchaseOrder.getOrderNo());
        vo.setVendorId(purchaseOrder.getVendorId());
        vo.setVendorNameSnapshot(purchaseOrder.getVendorNameSnapshot());
        vo.setPurchaserNameSnapshot(purchaseOrder.getPurchaserNameSnapshot());
        vo.setWarehouseNameSnapshot(purchaseOrder.getWarehouseNameSnapshot());
        vo.setCurrencyCode(purchaseOrder.getCurrencyCode());
        vo.setDeliveryDate(purchaseOrder.getDeliveryDate());
        vo.setBizStatus(purchaseOrder.getBizStatus());
        vo.setApprovalStatus(purchaseOrder.getApprovalStatus());
        vo.setExecutionStatus(purchaseOrder.getExecutionStatus());
        vo.setReceiptStatus(purchaseOrder.getReceiptStatus());
        vo.setInboundStatus(purchaseOrder.getInboundStatus());
        vo.setGrossAmount(purchaseOrder.getGrossAmount());
        vo.setNetAmount(purchaseOrder.getNetAmount());
        vo.setTaxAmount(purchaseOrder.getTaxAmount());
        vo.setInboundedQtySummary(purchaseOrder.getInboundedQtySummary());
        vo.setUninboundedQtySummary(purchaseOrder.getUninboundedQtySummary());
        vo.setClosedQtySummary(purchaseOrder.getClosedQtySummary());
        vo.setAddTime(purchaseOrder.getAddTime());
        vo.setUpdateTime(purchaseOrder.getUpdateTime());
        return vo;
    }

    public static PurchaseOrderSaveItemVO toSaveItemVO(PurchaseOrder purchaseOrder, List<PurchaseOrderItem> items) {
        PurchaseOrderSaveItemVO vo = new PurchaseOrderSaveItemVO();
        if (purchaseOrder == null) {
            vo.setSectionState(defaultSectionState());
            return vo;
        }
        PurchaseOrderMainDTO main = new PurchaseOrderMainDTO();
        main.setId(purchaseOrder.getId());
        main.setCorpid(purchaseOrder.getCorpid());
        main.setPurchaseOrgId(purchaseOrder.getPurchaseOrgId());
        main.setOrderNo(purchaseOrder.getOrderNo());
        main.setVendorId(purchaseOrder.getVendorId());
        main.setVendorNameSnapshot(purchaseOrder.getVendorNameSnapshot());
        main.setPurchaserId(purchaseOrder.getPurchaserId());
        main.setPurchaserNameSnapshot(purchaseOrder.getPurchaserNameSnapshot());
        main.setWarehouseId(purchaseOrder.getWarehouseId());
        main.setWarehouseNameSnapshot(purchaseOrder.getWarehouseNameSnapshot());
        main.setSettlementMethodId(purchaseOrder.getSettlementMethodId());
        main.setSettlementMethodSnapshot(purchaseOrder.getSettlementMethodSnapshot());
        main.setPaymentTermSnapshot(purchaseOrder.getPaymentTermSnapshot());
        main.setCurrencyCode(purchaseOrder.getCurrencyCode());
        main.setDeliveryDate(purchaseOrder.getDeliveryDate());
        main.setSourceType(purchaseOrder.getSourceType());
        main.setSourceNo(purchaseOrder.getSourceNo());
        main.setSalesLinkedFlag(purchaseOrder.getSalesLinkedFlag());
        main.setBizStatus(purchaseOrder.getBizStatus());
        main.setApprovalStatus(purchaseOrder.getApprovalStatus());
        main.setExecutionStatus(purchaseOrder.getExecutionStatus());
        main.setReceiptStatus(purchaseOrder.getReceiptStatus());
        main.setInboundStatus(purchaseOrder.getInboundStatus());
        main.setPayableStatus(purchaseOrder.getPayableStatus());
        main.setInvoiceStatus(purchaseOrder.getInvoiceStatus());
        main.setPaymentStatus(purchaseOrder.getPaymentStatus());
        main.setGrossAmount(purchaseOrder.getGrossAmount());
        main.setNetAmount(purchaseOrder.getNetAmount());
        main.setTaxAmount(purchaseOrder.getTaxAmount());
        main.setInboundedQtySummary(purchaseOrder.getInboundedQtySummary());
        main.setUninboundedQtySummary(purchaseOrder.getUninboundedQtySummary());
        main.setClosedQtySummary(purchaseOrder.getClosedQtySummary());
        main.setPayableAmountSummary(purchaseOrder.getPayableAmountSummary());
        main.setPaidAmountSummary(purchaseOrder.getPaidAmountSummary());
        main.setInvoicedAmountSummary(purchaseOrder.getInvoicedAmountSummary());
        main.setLastInboundTime(purchaseOrder.getLastInboundTime());
        main.setLastPayableTime(purchaseOrder.getLastPayableTime());
        main.setPeriodLockedFlag(purchaseOrder.getPeriodLockedFlag());
        main.setVersion(purchaseOrder.getVersion());
        main.setRemark(purchaseOrder.getRemark());
        main.setDeleted(purchaseOrder.getDeleted());
        main.setAddTime(purchaseOrder.getAddTime());
        main.setUpdateTime(purchaseOrder.getUpdateTime());
        main.setCreatorId(purchaseOrder.getCreatorId());
        main.setModifyId(purchaseOrder.getModifyId());
        vo.setMain(main);
        vo.setItems(toItemDTOs(items));
        vo.setSectionState(defaultSectionState());
        return vo;
    }

    public static PurchaseOrderDetailVO toDetailVO(PurchaseOrderSaveItemVO saveItemVO) {
        PurchaseOrderDetailVO detailVO = new PurchaseOrderDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }

    public static PurchaseOrderDraftListItemVO toDraftListItemVO(PurchaseOrderSaveDraftPojo pojo) {
        PurchaseOrderDraftListItemVO vo = new PurchaseOrderDraftListItemVO();
        vo.setDraftCode(pojo.getDraftCode());
        vo.setDraftTitle(pojo.getDraftTitle());
        if (pojo.getMain() != null) {
            vo.setOrderNo(pojo.getMain().getOrderNo());
            vo.setVendorId(pojo.getMain().getVendorId());
        }
        vo.setUpdatedTime(pojo.getUpdatedTime());
        return vo;
    }

    public static PurchaseOrderDraftDetailVO toDraftDetailVO(PurchaseOrderSaveDraftPojo pojo) {
        PurchaseOrderDraftDetailVO vo = new PurchaseOrderDraftDetailVO();
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

    public static PurchaseOrderItem toPurchaseOrderItem(String corpid, Long orderId, PurchaseOrderItemMainDTO item) {
        PurchaseOrderItem domain = new PurchaseOrderItem();
        domain.setId(item.getId());
        domain.setCorpid(corpid);
        domain.setOrderId(orderId);
        domain.setLineNo(item.getLineNo());
        domain.setSkuId(item.getSkuId());
        domain.setSkuCodeSnapshot(item.getSkuCodeSnapshot());
        domain.setSkuNameSnapshot(item.getSkuNameSnapshot());
        domain.setSpecSnapshot(item.getSpecSnapshot());
        domain.setPurchaseUnitId(item.getPurchaseUnitId());
        domain.setWarehouseId(item.getWarehouseId());
        domain.setOrderQty(item.getOrderQty());
        domain.setReceivedQty(item.getReceivedQty());
        domain.setInboundedQty(item.getInboundedQty());
        domain.setClosedQty(item.getClosedQty());
        domain.setReturnedQty(item.getReturnedQty());
        domain.setGrossPrice(item.getGrossPrice());
        domain.setNetPrice(item.getNetPrice());
        domain.setTaxRate(item.getTaxRate());
        domain.setTaxAmount(item.getTaxAmount());
        domain.setGrossAmount(item.getGrossAmount());
        domain.setNetAmount(item.getNetAmount());
        domain.setPayableAmount(item.getPayableAmount());
        domain.setPaidAmount(item.getPaidAmount());
        domain.setInvoicedAmount(item.getInvoicedAmount());
        domain.setIsGift(item.getIsGift());
        domain.setDeliveryPlanSnapshot(item.getDeliveryPlanSnapshot());
        domain.setVersion(item.getVersion());
        domain.setDeleted(item.getDeleted());
        domain.setAddTime(item.getAddTime());
        domain.setUpdateTime(item.getUpdateTime());
        domain.setCreatorId(item.getCreatorId());
        domain.setModifyId(item.getModifyId());
        return domain;
    }

    private static List<PurchaseOrderItemMainDTO> toItemDTOs(List<PurchaseOrderItem> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream().map(item -> {
            PurchaseOrderItemMainDTO dto = new PurchaseOrderItemMainDTO();
            dto.setId(item.getId());
            dto.setCorpid(item.getCorpid());
            dto.setOrderId(item.getOrderId());
            dto.setLineNo(item.getLineNo());
            dto.setSkuId(item.getSkuId());
            dto.setSkuCodeSnapshot(item.getSkuCodeSnapshot());
            dto.setSkuNameSnapshot(item.getSkuNameSnapshot());
            dto.setSpecSnapshot(item.getSpecSnapshot());
            dto.setPurchaseUnitId(item.getPurchaseUnitId());
            dto.setWarehouseId(item.getWarehouseId());
            dto.setOrderQty(item.getOrderQty());
            dto.setReceivedQty(item.getReceivedQty());
            dto.setInboundedQty(item.getInboundedQty());
            dto.setClosedQty(item.getClosedQty());
            dto.setReturnedQty(item.getReturnedQty());
            dto.setGrossPrice(item.getGrossPrice());
            dto.setNetPrice(item.getNetPrice());
            dto.setTaxRate(item.getTaxRate());
            dto.setTaxAmount(item.getTaxAmount());
            dto.setGrossAmount(item.getGrossAmount());
            dto.setNetAmount(item.getNetAmount());
            dto.setPayableAmount(item.getPayableAmount());
            dto.setPaidAmount(item.getPaidAmount());
            dto.setInvoicedAmount(item.getInvoicedAmount());
            dto.setIsGift(item.getIsGift());
            dto.setDeliveryPlanSnapshot(item.getDeliveryPlanSnapshot());
            dto.setVersion(item.getVersion());
            dto.setDeleted(item.getDeleted());
            dto.setAddTime(item.getAddTime());
            dto.setUpdateTime(item.getUpdateTime());
            dto.setCreatorId(item.getCreatorId());
            dto.setModifyId(item.getModifyId());
            return dto;
        }).toList();
    }

    private static PurchaseOrderSaveExtPojo toSaveExtPojo(List<PurchaseOrderItemMainDTO> items) {
        PurchaseOrderSaveExtPojo ext = new PurchaseOrderSaveExtPojo();
        ext.setItems(items == null ? List.of() : items);
        return ext;
    }

    private static PurchaseOrderSectionStatePojo toSectionStatePojo(PurchaseOrderSectionStateDTO dto) {
        PurchaseOrderSectionStatePojo pojo = new PurchaseOrderSectionStatePojo();
        if (dto != null) {
            pojo.setItems(dto.getItems());
        }
        return pojo;
    }

    private static PurchaseOrderDraftMetaPojo toDraftMetaPojo(xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderDraftMetaDTO dto) {
        PurchaseOrderDraftMetaPojo pojo = new PurchaseOrderDraftMetaPojo();
        if (dto != null) {
            pojo.setDraftCode(dto.getDraftCode());
            pojo.setDraftTitle(dto.getDraftTitle());
            pojo.setUpdatedTime(dto.getUpdatedTime());
        }
        return pojo;
    }

    private static PurchaseOrderSectionStateVO toSectionStateVO(PurchaseOrderSectionStatePojo pojo) {
        PurchaseOrderSectionStateVO vo = new PurchaseOrderSectionStateVO();
        vo.setItems(pojo.getItems());
        return vo;
    }

    private static PurchaseOrderDraftMetaVO toDraftMetaVO(PurchaseOrderSaveDraftPojo pojo) {
        PurchaseOrderDraftMetaVO vo = new PurchaseOrderDraftMetaVO();
        vo.setDraftCode(pojo.getDraftCode());
        vo.setDraftTitle(pojo.getDraftTitle());
        vo.setUpdatedTime(pojo.getUpdatedTime());
        return vo;
    }

    private static PurchaseOrderSectionStateVO defaultSectionState() {
        PurchaseOrderSectionStateVO vo = new PurchaseOrderSectionStateVO();
        vo.setItems(1);
        return vo;
    }
}
