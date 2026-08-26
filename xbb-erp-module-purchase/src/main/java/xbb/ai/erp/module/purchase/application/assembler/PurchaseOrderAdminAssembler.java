package xbb.ai.erp.module.purchase.application.assembler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import xbb.ai.erp.base.common.enums.AuditStatusEnum;
import xbb.ai.erp.base.common.enums.DocumentStatusEnum;
import xbb.ai.erp.base.common.enums.InboundStatusEnum;
import xbb.ai.erp.base.common.enums.InvoiceStatusEnum;
import xbb.ai.erp.base.common.enums.PaymentStatusEnum;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderItemDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseOrderSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseOrderSaveItemVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrder;
import xbb.ai.erp.module.purchase.domain.model.PurchaseOrderItem;

public final class PurchaseOrderAdminAssembler {

    private PurchaseOrderAdminAssembler() {
    }

    public static PurchaseOrderSaveItemVO buildEmptySaveItemVO() {
        PurchaseOrderSaveItemVO vo = new PurchaseOrderSaveItemVO();
        vo.setItems(List.of());
        return vo;
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
            purchaseOrder.setStatus(DocumentStatusEnum.OPEN.getCode());
            purchaseOrder.setAuditStatus(AuditStatusEnum.PENDING.getCode());
            purchaseOrder.setInboundStatus(InboundStatusEnum.NOT_INBOUNDED.getCode());
            purchaseOrder.setPaymentStatus(PaymentStatusEnum.NOT_PAID.getCode());
            purchaseOrder.setInvoiceStatus(InvoiceStatusEnum.NOT_INVOICED.getCode());
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
        vo.setSupplierId(Objects.isNull(purchaseOrder.getSupplierId()) ? "" : String.valueOf(purchaseOrder.getSupplierId()));
        vo.setSupplierName(purchaseOrder.getSupplierName());
        vo.setOrderDate(Objects.isNull(purchaseOrder.getOrderDate()) ? "" : String.valueOf(purchaseOrder.getOrderDate()));
        vo.setExpectedDate(Objects.isNull(purchaseOrder.getExpectedDate()) ? "" : String.valueOf(purchaseOrder.getExpectedDate()));
        vo.setTotalAmount(purchaseOrder.getTotalAmount());

        vo.setStatus(Objects.isNull(purchaseOrder.getStatus()) ? "" : String.valueOf(purchaseOrder.getStatus()));
        vo.setAuditStatus(Objects.isNull(purchaseOrder.getAuditStatus()) ? "" : String.valueOf(purchaseOrder.getAuditStatus()));
        vo.setInboundStatus(Objects.isNull(purchaseOrder.getInboundStatus()) ? "" : String.valueOf(purchaseOrder.getInboundStatus()));
        vo.setPaymentStatus(Objects.isNull(purchaseOrder.getPaymentStatus()) ? "" : String.valueOf(purchaseOrder.getPaymentStatus()));
        vo.setInvoiceStatus(Objects.isNull(purchaseOrder.getInvoiceStatus()) ? "" : String.valueOf(purchaseOrder.getInvoiceStatus()));
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
        main.setInvoiceStatus(purchaseOrder.getInvoiceStatus());
        main.setRemark(purchaseOrder.getRemark());
        main.setCreatorId(purchaseOrder.getCreatorId());
        main.setModifyId(purchaseOrder.getModifyId());
        vo.setMain(main);
        vo.setItems(List.of());
        return vo;
    }

    public static PurchaseOrderDetailVO toDetailVO(PurchaseOrderSaveItemVO saveItemVO) {
        PurchaseOrderDetailVO detailVO = new PurchaseOrderDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }

    public static PurchaseOrderItem toPurchaseOrderItem(PurchaseOrderItemDTO dto, String corpid,
                                                         Long purchaseOrderId, int lineNo, String userId) {
        PurchaseOrderItem item = new PurchaseOrderItem();
        item.setId(dto.getId());
        item.setCorpid(corpid);
        item.setPurchaseOrderId(purchaseOrderId);
        item.setLineNo(lineNo);
        item.setSkuId(dto.getSkuId());
        item.setSkuCode(dto.getSkuCode());
        item.setSkuName(dto.getSkuName());
        item.setSpecification(dto.getSpecification());
        item.setUnitName(dto.getUnitName());
        item.setQty(dto.getQty());
        item.setInboundQty(BigDecimal.ZERO);
        item.setUnitPrice(dto.getUnitPrice());
        item.setTaxRate(dto.getTaxRate() == null ? BigDecimal.ZERO : dto.getTaxRate());
        item.setAmount(calculateItemAmount(dto));
        item.setInboundStatus(InboundStatusEnum.NOT_INBOUNDED.getCode());
        item.setCreatorId(userId);
        item.setModifyId(userId);
        item.setWarehouseId(dto.getWarehouseId());
        return item;
    }

    public static BigDecimal calculateItemAmount(PurchaseOrderItemDTO dto) {
        return dto.getQty().multiply(dto.getUnitPrice()).setScale(2, RoundingMode.HALF_UP);
    }

    public static List<PurchaseOrderItemDTO> toPurchaseOrderItemDTOs(List<PurchaseOrderItem> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream().map(item -> {
            PurchaseOrderItemDTO dto = new PurchaseOrderItemDTO();
            dto.setId(item.getId());
            dto.setSkuId(item.getSkuId());
            dto.setSkuCode(item.getSkuCode());
            dto.setSkuName(item.getSkuName());
            dto.setSpecification(item.getSpecification());
            dto.setUnitName(item.getUnitName());
            dto.setQty(item.getQty());
            dto.setUnitPrice(item.getUnitPrice());
            dto.setTaxRate(item.getTaxRate());
            return dto;
        }).toList();
    }
}
