package xbb.ai.erp.module.sales.infrastructure.persistence.convertor;

import xbb.ai.erp.module.sales.domain.model.SalesOutboundItem;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOutboundItemPO;

public final class SalesOutboundItemConvertor {

    private SalesOutboundItemConvertor() {
    }

    public static SalesOutboundItemPO toPO(SalesOutboundItem salesOutboundItem) {
        if (salesOutboundItem == null) {
            return null;
        }
        SalesOutboundItemPO po = new SalesOutboundItemPO();
        po.setId(salesOutboundItem.getId());
        po.setCorpid(salesOutboundItem.getCorpid());
        po.setSalesOutboundId(salesOutboundItem.getSalesOutboundId());
        po.setSalesOrderItemId(salesOutboundItem.getSalesOrderItemId());
        po.setSkuId(salesOutboundItem.getSkuId());
        po.setSkuName(salesOutboundItem.getSkuName());
        po.setUnitName(salesOutboundItem.getUnitName());
        po.setQty(salesOutboundItem.getQty());
        po.setUnitPrice(salesOutboundItem.getUnitPrice());
        po.setAmount(salesOutboundItem.getAmount());
        po.setCostUnit(salesOutboundItem.getCostUnit());
        po.setCostAmount(salesOutboundItem.getCostAmount());
        po.setOutboundStatus(salesOutboundItem.getOutboundStatus());
        po.setCreatorId(salesOutboundItem.getCreatorId());
        po.setModifyId(salesOutboundItem.getModifyId());
        return po;
    }

    public static SalesOutboundItem toDomain(SalesOutboundItemPO po) {
        if (po == null) {
            return null;
        }
        SalesOutboundItem salesOutboundItem = new SalesOutboundItem();
        salesOutboundItem.setId(po.getId());
        salesOutboundItem.setCorpid(po.getCorpid());
        salesOutboundItem.setSalesOutboundId(po.getSalesOutboundId());
        salesOutboundItem.setSalesOrderItemId(po.getSalesOrderItemId());
        salesOutboundItem.setSkuId(po.getSkuId());
        salesOutboundItem.setSkuName(po.getSkuName());
        salesOutboundItem.setUnitName(po.getUnitName());
        salesOutboundItem.setQty(po.getQty());
        salesOutboundItem.setUnitPrice(po.getUnitPrice());
        salesOutboundItem.setAmount(po.getAmount());
        salesOutboundItem.setCostUnit(po.getCostUnit());
        salesOutboundItem.setCostAmount(po.getCostAmount());
        salesOutboundItem.setOutboundStatus(po.getOutboundStatus());
        salesOutboundItem.setCreatorId(po.getCreatorId());
        salesOutboundItem.setModifyId(po.getModifyId());
        return salesOutboundItem;
    }
}
