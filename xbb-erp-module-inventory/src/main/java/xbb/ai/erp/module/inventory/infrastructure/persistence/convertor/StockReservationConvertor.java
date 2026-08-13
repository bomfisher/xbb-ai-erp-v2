package xbb.ai.erp.module.inventory.infrastructure.persistence.convertor;

import xbb.ai.erp.module.inventory.domain.model.StockReservation;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockReservationPO;

public final class StockReservationConvertor {

    private StockReservationConvertor() {
    }

    public static StockReservationPO toPO(StockReservation stockReservation) {
        if (stockReservation == null) {
            return null;
        }
        StockReservationPO po = new StockReservationPO();
        po.setId(stockReservation.getId());
        po.setCorpid(stockReservation.getCorpid());
        po.setWarehouseId(stockReservation.getWarehouseId());
        po.setSkuId(stockReservation.getSkuId());
        po.setSourceType(stockReservation.getSourceType());
        po.setSourceId(stockReservation.getSourceId());
        po.setSourceLineId(stockReservation.getSourceLineId());
        po.setReservedQty(stockReservation.getReservedQty());
        po.setOutboundQty(stockReservation.getOutboundQty());
        po.setReleasedQty(stockReservation.getReleasedQty());
        po.setRemainingQty(stockReservation.getRemainingQty());
        po.setStatus(stockReservation.getStatus());
        po.setReservedAt(stockReservation.getReservedAt());
        po.setReleasedAt(stockReservation.getReleasedAt());
        po.setIdempotencyKey(stockReservation.getIdempotencyKey());
        po.setVersion(stockReservation.getVersion());
        po.setDel(stockReservation.getDel());
        po.setAddTime(stockReservation.getAddTime());
        po.setUpdateTime(stockReservation.getUpdateTime());
        po.setCreatorId(stockReservation.getCreatorId());
        po.setModifyId(stockReservation.getModifyId());
        return po;
    }

    public static StockReservation toDomain(StockReservationPO po) {
        if (po == null) {
            return null;
        }
        StockReservation stockReservation = new StockReservation();
        stockReservation.setId(po.getId());
        stockReservation.setCorpid(po.getCorpid());
        stockReservation.setWarehouseId(po.getWarehouseId());
        stockReservation.setSkuId(po.getSkuId());
        stockReservation.setSourceType(po.getSourceType());
        stockReservation.setSourceId(po.getSourceId());
        stockReservation.setSourceLineId(po.getSourceLineId());
        stockReservation.setReservedQty(po.getReservedQty());
        stockReservation.setOutboundQty(po.getOutboundQty());
        stockReservation.setReleasedQty(po.getReleasedQty());
        stockReservation.setRemainingQty(po.getRemainingQty());
        stockReservation.setStatus(po.getStatus());
        stockReservation.setReservedAt(po.getReservedAt());
        stockReservation.setReleasedAt(po.getReleasedAt());
        stockReservation.setIdempotencyKey(po.getIdempotencyKey());
        stockReservation.setVersion(po.getVersion());
        stockReservation.setDel(po.getDel());
        stockReservation.setAddTime(po.getAddTime());
        stockReservation.setUpdateTime(po.getUpdateTime());
        stockReservation.setCreatorId(po.getCreatorId());
        stockReservation.setModifyId(po.getModifyId());
        return stockReservation;
    }
}
