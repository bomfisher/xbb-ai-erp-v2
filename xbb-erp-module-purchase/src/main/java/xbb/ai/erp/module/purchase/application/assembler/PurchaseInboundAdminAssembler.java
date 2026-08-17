package xbb.ai.erp.module.purchase.application.assembler;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Objects;

import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundMainDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundItemDTO;
import xbb.ai.erp.module.purchase.admin.dto.PurchaseInboundSaveDTO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundDetailVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundListItemVO;
import xbb.ai.erp.module.purchase.admin.vo.PurchaseInboundSaveItemVO;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInbound;
import xbb.ai.erp.module.purchase.domain.model.PurchaseInboundItem;

public final class PurchaseInboundAdminAssembler {

    private PurchaseInboundAdminAssembler() {
    }

    public static PurchaseInboundSaveItemVO buildEmptySaveItemVO() {
        PurchaseInboundSaveItemVO vo = new PurchaseInboundSaveItemVO();
        vo.setItems(List.of());
        return vo;
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
        vo.setInboundDate(Objects.isNull(purchaseInbound.getInboundDate()) ? "" : String.valueOf(purchaseInbound.getInboundDate()));
        vo.setTotalAmount(purchaseInbound.getTotalAmount());
        vo.setStatus(purchaseInbound.getStatus());
        vo.setAuditStatus(Objects.isNull(purchaseInbound.getAuditStatus()) ? "" : String.valueOf(purchaseInbound.getAuditStatus()));
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
        vo.setItems(List.of());
        return vo;
    }

    public static PurchaseInboundDetailVO toDetailVO(PurchaseInboundSaveItemVO saveItemVO) {
        PurchaseInboundDetailVO detailVO = new PurchaseInboundDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }

    public static PurchaseInboundItem toPurchaseInboundItem(PurchaseInboundItemDTO dto, String corpid,
                                                             Long purchaseInboundId, String userId) {
        PurchaseInboundItem item = new PurchaseInboundItem();
        item.setId(dto.getId());
        item.setCorpid(corpid);
        item.setPurchaseInboundId(purchaseInboundId);
        item.setPurchaseOrderItemId(dto.getPurchaseOrderItemId());
        item.setSkuId(dto.getSkuId());
        item.setSkuName(dto.getSkuName());
        item.setUnitName(dto.getUnitName());
        item.setWarehouseId(dto.getWarehouseId());
        item.setQty(dto.getQty());
        item.setUnitPrice(dto.getUnitPrice());
        item.setAmount(dto.getQty().multiply(dto.getUnitPrice()).setScale(2, RoundingMode.HALF_UP));
        BigDecimal costUnit = dto.getCostUnit() == null ? dto.getUnitPrice() : dto.getCostUnit();
        item.setCostUnit(costUnit);
        item.setCostAmount(dto.getQty().multiply(costUnit).setScale(2, RoundingMode.HALF_UP));
        item.setCreatorId(userId);
        item.setModifyId(userId);
        return item;
    }

    public static List<PurchaseInboundItemDTO> toPurchaseInboundItemDTOs(List<PurchaseInboundItem> items) {
        if (items == null) {
            return List.of();
        }
        return items.stream().map(item -> {
            PurchaseInboundItemDTO dto = new PurchaseInboundItemDTO();
            dto.setId(item.getId());
            dto.setPurchaseOrderItemId(item.getPurchaseOrderItemId());
            dto.setSkuId(item.getSkuId());
            dto.setSkuName(item.getSkuName());
            dto.setUnitName(item.getUnitName());
            dto.setWarehouseId(item.getWarehouseId());
            dto.setQty(item.getQty());
            dto.setUnitPrice(item.getUnitPrice());
            dto.setCostUnit(item.getCostUnit());
            return dto;
        }).toList();
    }
}
