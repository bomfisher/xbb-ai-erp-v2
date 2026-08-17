package xbb.ai.erp.module.sales.application.assembler;

import java.util.Objects;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundMainDTO;
import xbb.ai.erp.module.sales.admin.dto.SalesOutboundSaveDTO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundDetailVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundListItemVO;
import xbb.ai.erp.module.sales.admin.vo.SalesOutboundSaveItemVO;
import xbb.ai.erp.module.sales.domain.model.SalesOutbound;

public final class SalesOutboundAdminAssembler {

    private SalesOutboundAdminAssembler() {
    }

    public static SalesOutboundSaveItemVO buildEmptySaveItemVO() {
        return new SalesOutboundSaveItemVO();
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
            salesOutbound.setCustomerName(main.getCustomerName());
            salesOutbound.setWarehouseId(main.getWarehouseId());
            salesOutbound.setOutboundDate(main.getOutboundDate());
            salesOutbound.setTotalAmount(main.getTotalAmount());
            salesOutbound.setStatus(main.getStatus());
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
        vo.setStatus(salesOutbound.getStatus());
        vo.setRemark(salesOutbound.getRemark());
        vo.setAuditStatus(Objects.isNull(salesOutbound.getAuditStatus()) ? "" : Objects.toString(salesOutbound.getAuditStatus()));
        vo.setCreatorId(salesOutbound.getCreatorId());
        vo.setModifyId(salesOutbound.getModifyId());
        return vo;
    }

    public static SalesOutboundSaveItemVO toSaveItemVO(SalesOutbound salesOutbound) {
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
        main.setWarehouseId(salesOutbound.getWarehouseId());
        main.setOutboundDate(salesOutbound.getOutboundDate());
        main.setTotalAmount(salesOutbound.getTotalAmount());
        main.setStatus(salesOutbound.getStatus());
        main.setRemark(salesOutbound.getRemark());
        main.setAuditStatus(salesOutbound.getAuditStatus());
        main.setCreatorId(salesOutbound.getCreatorId());
        main.setModifyId(salesOutbound.getModifyId());
        vo.setMain(main);
        return vo;
    }

    public static SalesOutboundDetailVO toDetailVO(SalesOutboundSaveItemVO saveItemVO) {
        SalesOutboundDetailVO detailVO = new SalesOutboundDetailVO();
        detailVO.setMainData(saveItemVO);
        return detailVO;
    }
}
