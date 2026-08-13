package xbb.ai.erp.module.inventory.infrastructure.persistence.convertor;

import xbb.ai.erp.module.inventory.domain.model.StockCostTransaction;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockCostTransactionPO;

public final class StockCostTransactionConvertor {

    private StockCostTransactionConvertor() {
    }

    public static StockCostTransactionPO toPO(StockCostTransaction stockCostTransaction) {
        if (stockCostTransaction == null) {
            return null;
        }
        StockCostTransactionPO po = new StockCostTransactionPO();
        po.setId(stockCostTransaction.getId());
        po.setCorpid(stockCostTransaction.getCorpid());
        po.setWarehouseId(stockCostTransaction.getWarehouseId());
        po.setSkuId(stockCostTransaction.getSkuId());
        po.setActionType(stockCostTransaction.getActionType());
        po.setBusinessCode(stockCostTransaction.getBusinessCode());
        po.setSourceId(stockCostTransaction.getSourceId());
        po.setQtyBefore(stockCostTransaction.getQtyBefore());
        po.setQtyChange(stockCostTransaction.getQtyChange());
        po.setQtyAfter(stockCostTransaction.getQtyAfter());
        po.setTotalCostBefore(stockCostTransaction.getTotalCostBefore());
        po.setTotalCostChange(stockCostTransaction.getTotalCostChange());
        po.setTotalCostAfter(stockCostTransaction.getTotalCostAfter());
        po.setUnitCostBefore(stockCostTransaction.getUnitCostBefore());
        po.setUnitCost(stockCostTransaction.getUnitCost());
        po.setUnitCostAfter(stockCostTransaction.getUnitCostAfter());
        po.setTailDifference(stockCostTransaction.getTailDifference());
        po.setReason(stockCostTransaction.getReason());
        po.setIdempotencyKey(stockCostTransaction.getIdempotencyKey());
        po.setOperatorId(stockCostTransaction.getOperatorId());
        po.setOccurredAt(stockCostTransaction.getOccurredAt());
        po.setDel(stockCostTransaction.getDel());
        po.setAddTime(stockCostTransaction.getAddTime());
        po.setUpdateTime(stockCostTransaction.getUpdateTime());
        po.setCreatorId(stockCostTransaction.getCreatorId());
        po.setModifyId(stockCostTransaction.getModifyId());
        return po;
    }

    public static StockCostTransaction toDomain(StockCostTransactionPO po) {
        if (po == null) {
            return null;
        }
        StockCostTransaction stockCostTransaction = new StockCostTransaction();
        stockCostTransaction.setId(po.getId());
        stockCostTransaction.setCorpid(po.getCorpid());
        stockCostTransaction.setWarehouseId(po.getWarehouseId());
        stockCostTransaction.setSkuId(po.getSkuId());
        stockCostTransaction.setActionType(po.getActionType());
        stockCostTransaction.setBusinessCode(po.getBusinessCode());
        stockCostTransaction.setSourceId(po.getSourceId());
        stockCostTransaction.setQtyBefore(po.getQtyBefore());
        stockCostTransaction.setQtyChange(po.getQtyChange());
        stockCostTransaction.setQtyAfter(po.getQtyAfter());
        stockCostTransaction.setTotalCostBefore(po.getTotalCostBefore());
        stockCostTransaction.setTotalCostChange(po.getTotalCostChange());
        stockCostTransaction.setTotalCostAfter(po.getTotalCostAfter());
        stockCostTransaction.setUnitCostBefore(po.getUnitCostBefore());
        stockCostTransaction.setUnitCost(po.getUnitCost());
        stockCostTransaction.setUnitCostAfter(po.getUnitCostAfter());
        stockCostTransaction.setTailDifference(po.getTailDifference());
        stockCostTransaction.setReason(po.getReason());
        stockCostTransaction.setIdempotencyKey(po.getIdempotencyKey());
        stockCostTransaction.setOperatorId(po.getOperatorId());
        stockCostTransaction.setOccurredAt(po.getOccurredAt());
        stockCostTransaction.setDel(po.getDel());
        stockCostTransaction.setAddTime(po.getAddTime());
        stockCostTransaction.setUpdateTime(po.getUpdateTime());
        stockCostTransaction.setCreatorId(po.getCreatorId());
        stockCostTransaction.setModifyId(po.getModifyId());
        return stockCostTransaction;
    }
}
