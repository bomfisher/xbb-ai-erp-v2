package xbb.ai.erp.module.inventory.domain.repository;

import xbb.ai.erp.module.inventory.domain.model.StockReservation;

import java.util.List;
import java.util.Map;

public interface StockReservationRepository {
    Long insert(StockReservation stockReservation);

    void insertBatch(List<StockReservation> stockReservationList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(StockReservation stockReservation);

    StockReservation findById(String corpid, Long id);

    List<StockReservation> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
