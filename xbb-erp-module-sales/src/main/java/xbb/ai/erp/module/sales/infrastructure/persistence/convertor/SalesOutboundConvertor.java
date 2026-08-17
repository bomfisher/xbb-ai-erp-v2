package xbb.ai.erp.module.sales.infrastructure.persistence.convertor;

import xbb.ai.erp.module.sales.domain.model.SalesOutbound;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOutboundPO;

public final class SalesOutboundConvertor {

    private SalesOutboundConvertor() {
    }

    public static SalesOutboundPO toPO(SalesOutbound salesOutbound) {
        if (salesOutbound == null) {
            return null;
        }
        SalesOutboundPO po = new SalesOutboundPO();
        po.setId(salesOutbound.getId());
        po.setCorpid(salesOutbound.getCorpid());
        po.setOutboundNo(salesOutbound.getOutboundNo());
        po.setSalesOrderId(salesOutbound.getSalesOrderId());
        po.setCustomerId(salesOutbound.getCustomerId());
        po.setCustomerName(salesOutbound.getCustomerName());
        po.setWarehouseId(salesOutbound.getWarehouseId());
        po.setOutboundDate(salesOutbound.getOutboundDate());
        po.setTotalAmount(salesOutbound.getTotalAmount());
        po.setStatus(salesOutbound.getStatus());
        po.setRemark(salesOutbound.getRemark());
        po.setAuditStatus(salesOutbound.getAuditStatus());
        po.setCreatorId(salesOutbound.getCreatorId());
        po.setModifyId(salesOutbound.getModifyId());
        return po;
    }

    public static SalesOutbound toDomain(SalesOutboundPO po) {
        if (po == null) {
            return null;
        }
        SalesOutbound salesOutbound = new SalesOutbound();
        salesOutbound.setId(po.getId());
        salesOutbound.setCorpid(po.getCorpid());
        salesOutbound.setOutboundNo(po.getOutboundNo());
        salesOutbound.setSalesOrderId(po.getSalesOrderId());
        salesOutbound.setCustomerId(po.getCustomerId());
        salesOutbound.setCustomerName(po.getCustomerName());
        salesOutbound.setWarehouseId(po.getWarehouseId());
        salesOutbound.setOutboundDate(po.getOutboundDate());
        salesOutbound.setTotalAmount(po.getTotalAmount());
        salesOutbound.setStatus(po.getStatus());
        salesOutbound.setRemark(po.getRemark());
        salesOutbound.setAuditStatus(po.getAuditStatus());
        salesOutbound.setCreatorId(po.getCreatorId());
        salesOutbound.setModifyId(po.getModifyId());
        return salesOutbound;
    }
}
