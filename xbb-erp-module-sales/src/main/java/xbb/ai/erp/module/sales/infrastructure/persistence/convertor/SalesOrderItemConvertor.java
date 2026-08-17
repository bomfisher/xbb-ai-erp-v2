package xbb.ai.erp.module.sales.infrastructure.persistence.convertor;

import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOrderItemPO;

public final class SalesOrderItemConvertor {

    private SalesOrderItemConvertor() {
    }

    public static SalesOrderItemPO toPO(SalesOrderItem salesOrderItem) {
        if (salesOrderItem == null) {
            return null;
        }
        SalesOrderItemPO po = new SalesOrderItemPO();
        po.setId(salesOrderItem.getId());
        po.setCorpid(salesOrderItem.getCorpid());
        po.setSalesOrderId(salesOrderItem.getSalesOrderId());
        po.setWarehouseId(salesOrderItem.getWarehouseId());
        po.setLineNo(salesOrderItem.getLineNo());
        po.setSkuId(salesOrderItem.getSkuId());
        po.setSkuCode(salesOrderItem.getSkuCode());
        po.setSkuName(salesOrderItem.getSkuName());
        po.setSpecification(salesOrderItem.getSpecification());
        po.setUnitName(salesOrderItem.getUnitName());
        po.setQty(salesOrderItem.getQty());
        po.setDeliveredQty(salesOrderItem.getDeliveredQty());
        po.setUnitPrice(salesOrderItem.getUnitPrice());
        po.setTaxRate(salesOrderItem.getTaxRate());
        po.setAmount(salesOrderItem.getAmount());
        po.setOutboundStatus(salesOrderItem.getOutboundStatus());
        po.setCreatorId(salesOrderItem.getCreatorId());
        po.setModifyId(salesOrderItem.getModifyId());
        return po;
    }

    public static SalesOrderItem toDomain(SalesOrderItemPO po) {
        if (po == null) {
            return null;
        }
        SalesOrderItem salesOrderItem = new SalesOrderItem();
        salesOrderItem.setId(po.getId());
        salesOrderItem.setCorpid(po.getCorpid());
        salesOrderItem.setSalesOrderId(po.getSalesOrderId());
        salesOrderItem.setWarehouseId(po.getWarehouseId());
        salesOrderItem.setLineNo(po.getLineNo());
        salesOrderItem.setSkuId(po.getSkuId());
        salesOrderItem.setSkuCode(po.getSkuCode());
        salesOrderItem.setSkuName(po.getSkuName());
        salesOrderItem.setSpecification(po.getSpecification());
        salesOrderItem.setUnitName(po.getUnitName());
        salesOrderItem.setQty(po.getQty());
        salesOrderItem.setDeliveredQty(po.getDeliveredQty());
        salesOrderItem.setUnitPrice(po.getUnitPrice());
        salesOrderItem.setTaxRate(po.getTaxRate());
        salesOrderItem.setAmount(po.getAmount());
        salesOrderItem.setOutboundStatus(po.getOutboundStatus());
        salesOrderItem.setCreatorId(po.getCreatorId());
        salesOrderItem.setModifyId(po.getModifyId());
        return salesOrderItem;
    }
}
