package xbb.ai.erp.module.sales.application.assembler;

import java.util.Objects;

import xbb.ai.erp.base.common.enums.DocumentStatusEnum;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundMainDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSaveDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO;
import xbb.ai.erp.module.sales.domain.model.SalesOutbound;
import xbb.ai.erp.module.sales.domain.model.SalesOutboundItem;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundItemDTO;
import java.util.List;

public final class SalesOutboundAdminAssembler {

    private SalesOutboundAdminAssembler() {
    }

    public static SalesOutboundSaveItemVO buildEmptySaveItemVO() {
        SalesOutboundSaveItemVO vo = new SalesOutboundSaveItemVO();
        vo.setMain(new SalesOutboundMainDTO());
        vo.setItems(List.of());
        return vo;
    }

    public static SalesOutbound toSalesOutbound(SalesOutboundSaveDTO dto) {
        SalesOutbound salesOutbound = new SalesOutbound();
        SalesOutboundMainDTO main = dto.getMain();
        if (main != null) {
            salesOutbound.setId(main.getId());
            salesOutbound.setCorpid(main.getCorpid());
            salesOutbound.setOutboundNo(main.getOutboundNo());
            salesOutbound.setSalesOrderId(main.getSalesOrderId());
            salesOutbound.setCustomerId(main.getCustomerId());
            salesOutbound.setCustomerName(main.getCustomerName() == null ? main.getCustomerLabel() : main.getCustomerName());
            salesOutbound.setWarehouseId(main.getWarehouseId());
            salesOutbound.setOutboundDate(main.getOutboundDate());
            salesOutbound.setTotalAmount(main.getTotalAmount());
            salesOutbound.setStatus(DocumentStatusEnum.OPEN.getCode());
            salesOutbound.setRemark(main.getRemark());
            salesOutbound.setAuditStatus(main.getAuditStatus());
            salesOutbound.setCreatorId(main.getCreatorId());
            salesOutbound.setModifyId(main.getModifyId());
            if (Objects.isNull(main.getId())) {
                salesOutbound.setCreatorId(dto.getUserId());
            }
            salesOutbound.setModifyId(dto.getUserId());
        }
        salesOutbound.setCorpid(dto.getCorpid());
        return salesOutbound;
    }

    public static SalesOutboundListItemVO toListItemVO(SalesOutbound salesOutbound) {
        SalesOutboundListItemVO vo = new SalesOutboundListItemVO();
        vo.setId(Objects.isNull(salesOutbound.getId()) ? "" : Objects.toString(salesOutbound.getId()));
        vo.setOutboundNo(salesOutbound.getOutboundNo());
        vo.setSalesOrderId(Objects.isNull(salesOutbound.getSalesOrderId()) ? "" : Objects.toString(salesOutbound.getSalesOrderId()));
        vo.setCustomerId(Objects.isNull(salesOutbound.getCustomerId()) ? "" : Objects.toString(salesOutbound.getCustomerId()));
        vo.setCustomerName(salesOutbound.getCustomerName());
        vo.setWarehouseId(Objects.isNull(salesOutbound.getWarehouseId()) ? "" : Objects.toString(salesOutbound.getWarehouseId()));
        vo.setOutboundDate(Objects.isNull(salesOutbound.getOutboundDate()) ? "" : java.time.Instant.ofEpochMilli(salesOutbound.getOutboundDate()).atZone(java.time.ZoneId.systemDefault()).toLocalDate().toString());
        vo.setTotalAmount(Objects.isNull(salesOutbound.getTotalAmount()) ? "" : Objects.toString(salesOutbound.getTotalAmount()));
        vo.setStatus(Objects.isNull(salesOutbound.getStatus()) ? "" : Objects.toString(salesOutbound.getStatus()));
        vo.setRemark(salesOutbound.getRemark());
        vo.setAuditStatus(Objects.isNull(salesOutbound.getAuditStatus()) ? "" : Objects.toString(salesOutbound.getAuditStatus()));
        vo.setCreatorId(salesOutbound.getCreatorId());
        vo.setModifyId(salesOutbound.getModifyId());
        return vo;
    }

    public static SalesOutboundSaveItemVO toSaveItemVO(SalesOutbound salesOutbound) {
        return toSaveItemVO(salesOutbound, List.of());
    }

    public static SalesOutboundSaveItemVO toSaveItemVO(SalesOutbound salesOutbound, List<SalesOutboundItem> items) {
        SalesOutboundSaveItemVO vo = new SalesOutboundSaveItemVO();
        if (salesOutbound == null) {
            return vo;
        }
        SalesOutboundMainDTO main = new SalesOutboundMainDTO();
        main.setId(salesOutbound.getId());
        main.setCorpid(salesOutbound.getCorpid());
        main.setOutboundNo(salesOutbound.getOutboundNo());
        main.setSalesOrderId(salesOutbound.getSalesOrderId());
        main.setCustomerId(salesOutbound.getCustomerId());
        main.setCustomerName(salesOutbound.getCustomerName());
        main.setCustomerLabel(salesOutbound.getCustomerName());
        main.setWarehouseId(salesOutbound.getWarehouseId());
        main.setOutboundDate(salesOutbound.getOutboundDate());
        main.setTotalAmount(salesOutbound.getTotalAmount());
        main.setStatus(Objects.isNull(salesOutbound.getStatus()) ? null : Objects.toString(salesOutbound.getStatus()));
        main.setRemark(salesOutbound.getRemark());
        main.setAuditStatus(salesOutbound.getAuditStatus());
        main.setCreatorId(salesOutbound.getCreatorId());
        main.setModifyId(salesOutbound.getModifyId());
        vo.setMain(main);
        vo.setItems(items.stream().map(item -> {
            SalesOutboundItemDTO dto = new SalesOutboundItemDTO();
            dto.setId(item.getId());
            dto.setSalesOrderItemId(item.getSalesOrderItemId());
            dto.setSkuId(item.getSkuId());
            dto.setSkuName(item.getSkuName());
            dto.setWarehouseId(item.getWarehouseId());
            dto.setUnitName(item.getUnitName());
            dto.setQty(item.getQty());
            dto.setUnitPrice(item.getUnitPrice());
            dto.setAmount(item.getAmount());
            dto.setCostUnit(item.getCostUnit());
            dto.setCostAmount(item.getCostAmount());
            return dto;
        }).toList());
        return vo;
    }

    public static SalesOutboundDetailVO toDetailVO(SalesOutboundSaveItemVO saveItemVO) {
        SalesOutboundDetailVO detailVO = new SalesOutboundDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
