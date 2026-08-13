package xbb.ai.erp.module.inventory.infrastructure.persistence.convertor;

import xbb.ai.erp.module.inventory.domain.model.StockTransaction;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockTransactionPO;

public final class StockTransactionConvertor {

    private StockTransactionConvertor() {
    }

    public static StockTransactionPO toPO(StockTransaction stockTransaction) {
        if (stockTransaction == null) {
            return null;
        }
        StockTransactionPO po = new StockTransactionPO();
        po.setId(stockTransaction.getId());
        po.setCorpid(stockTransaction.getCorpid());
        po.setWarehouseId(stockTransaction.getWarehouseId());
        po.setSkuId(stockTransaction.getSkuId());
        po.setActionType(stockTransaction.getActionType());
        po.setQtyBefore(stockTransaction.getQtyBefore());
        po.setQtyChange(stockTransaction.getQtyChange());
        po.setQtyAfter(stockTransaction.getQtyAfter());
        po.setSourceType(stockTransaction.getSourceType());
        po.setSourceId(stockTransaction.getSourceId());
        po.setIdempotencyKey(stockTransaction.getIdempotencyKey());
        po.setOperatorId(stockTransaction.getOperatorId());
        po.setOccurredAt(stockTransaction.getOccurredAt());
        po.setDel(stockTransaction.getDel());
        po.setAddTime(stockTransaction.getAddTime());
        po.setUpdateTime(stockTransaction.getUpdateTime());
        po.setCreatorId(stockTransaction.getCreatorId());
        po.setModifyId(stockTransaction.getModifyId());
        return po;
    }

    public static StockTransaction toDomain(StockTransactionPO po) {
        if (po == null) {
            return null;
        }
        StockTransaction stockTransaction = new StockTransaction();
        stockTransaction.setId(po.getId());
        stockTransaction.setCorpid(po.getCorpid());
        stockTransaction.setWarehouseId(po.getWarehouseId());
        stockTransaction.setSkuId(po.getSkuId());
        stockTransaction.setActionType(po.getActionType());
        stockTransaction.setQtyBefore(po.getQtyBefore());
        stockTransaction.setQtyChange(po.getQtyChange());
        stockTransaction.setQtyAfter(po.getQtyAfter());
        stockTransaction.setSourceType(po.getSourceType());
        stockTransaction.setSourceId(po.getSourceId());
        stockTransaction.setIdempotencyKey(po.getIdempotencyKey());
        stockTransaction.setOperatorId(po.getOperatorId());
        stockTransaction.setOccurredAt(po.getOccurredAt());
        stockTransaction.setDel(po.getDel());
        stockTransaction.setAddTime(po.getAddTime());
        stockTransaction.setUpdateTime(po.getUpdateTime());
        stockTransaction.setCreatorId(po.getCreatorId());
        stockTransaction.setModifyId(po.getModifyId());
        return stockTransaction;
    }
}
