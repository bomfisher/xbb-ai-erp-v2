package xbb.ai.erp.module.sales.infrastructure.persistence.convertor;

import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOrderPO;

public final class SalesOrderConvertor {

    private SalesOrderConvertor() {
    }

    public static SalesOrderPO toPO(SalesOrder salesOrder) {
        if (salesOrder == null) {
            return null;
        }
        SalesOrderPO po = new SalesOrderPO();
        po.setId(salesOrder.getId());
        po.setCorpid(salesOrder.getCorpid());
        po.setOrderNo(salesOrder.getOrderNo());
        po.setCustomerId(salesOrder.getCustomerId());
        po.setWarehouseId(salesOrder.getWarehouseId());
        po.setOrderDate(salesOrder.getOrderDate());
        po.setDeliveryDate(salesOrder.getDeliveryDate());
        po.setTotalAmount(salesOrder.getTotalAmount());
        po.setStatus(salesOrder.getStatus());
        po.setRemark(salesOrder.getRemark());
        po.setAuditStatus(salesOrder.getAuditStatus());
        po.setOutboundStatus(salesOrder.getOutboundStatus());
        po.setReceiptStatus(salesOrder.getReceiptStatus());
        po.setInvoiceStatus(salesOrder.getInvoiceStatus());
        po.setCreatorId(salesOrder.getCreatorId());
        po.setModifyId(salesOrder.getModifyId());
        return po;
    }

    public static SalesOrder toDomain(SalesOrderPO po) {
        if (po == null) {
            return null;
        }
        SalesOrder salesOrder = new SalesOrder();
        salesOrder.setId(po.getId());
        salesOrder.setCorpid(po.getCorpid());
        salesOrder.setOrderNo(po.getOrderNo());
        salesOrder.setCustomerId(po.getCustomerId());
        salesOrder.setWarehouseId(po.getWarehouseId());
        salesOrder.setOrderDate(po.getOrderDate());
        salesOrder.setDeliveryDate(po.getDeliveryDate());
        salesOrder.setTotalAmount(po.getTotalAmount());
        salesOrder.setStatus(po.getStatus());
        salesOrder.setRemark(po.getRemark());
        salesOrder.setAuditStatus(po.getAuditStatus());
        salesOrder.setOutboundStatus(po.getOutboundStatus());
        salesOrder.setReceiptStatus(po.getReceiptStatus());
        salesOrder.setInvoiceStatus(po.getInvoiceStatus());
        salesOrder.setCreatorId(po.getCreatorId());
        salesOrder.setModifyId(po.getModifyId());
        return salesOrder;
    }
}
