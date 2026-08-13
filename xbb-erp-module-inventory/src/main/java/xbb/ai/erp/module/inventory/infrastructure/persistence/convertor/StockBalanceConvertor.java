package xbb.ai.erp.module.inventory.infrastructure.persistence.convertor;

import xbb.ai.erp.module.inventory.domain.model.StockBalance;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockBalancePO;

public final class StockBalanceConvertor {

    private StockBalanceConvertor() {
    }

    public static StockBalancePO toPO(StockBalance stockBalance) {
        if (stockBalance == null) {
            return null;
        }
        StockBalancePO po = new StockBalancePO();
        po.setId(stockBalance.getId());
        po.setCorpid(stockBalance.getCorpid());
        po.setWarehouseId(stockBalance.getWarehouseId());
        po.setSkuId(stockBalance.getSkuId());
        po.setQty(stockBalance.getQty());
        po.setLockedQty(stockBalance.getLockedQty());
        po.setAvailableQty(stockBalance.getAvailableQty());
        po.setTotalCost(stockBalance.getTotalCost());
        po.setUnitCost(stockBalance.getUnitCost());
        po.setVersion(stockBalance.getVersion());
        po.setDel(stockBalance.getDel());
        po.setAddTime(stockBalance.getAddTime());
        po.setUpdateTime(stockBalance.getUpdateTime());
        po.setCreatorId(stockBalance.getCreatorId());
        po.setModifyId(stockBalance.getModifyId());
        return po;
    }

    public static StockBalance toDomain(StockBalancePO po) {
        if (po == null) {
            return null;
        }
        StockBalance stockBalance = new StockBalance();
        stockBalance.setId(po.getId());
        stockBalance.setCorpid(po.getCorpid());
        stockBalance.setWarehouseId(po.getWarehouseId());
        stockBalance.setSkuId(po.getSkuId());
        stockBalance.setQty(po.getQty());
        stockBalance.setLockedQty(po.getLockedQty());
        stockBalance.setAvailableQty(po.getAvailableQty());
        stockBalance.setTotalCost(po.getTotalCost());
        stockBalance.setUnitCost(po.getUnitCost());
        stockBalance.setVersion(po.getVersion());
        stockBalance.setDel(po.getDel());
        stockBalance.setAddTime(po.getAddTime());
        stockBalance.setUpdateTime(po.getUpdateTime());
        stockBalance.setCreatorId(po.getCreatorId());
        stockBalance.setModifyId(po.getModifyId());
        return stockBalance;
    }
}
