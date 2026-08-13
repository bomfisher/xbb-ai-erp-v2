package xbb.ai.erp.module.inventory.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.inventory.domain.model.StockReservation;
import xbb.ai.erp.module.inventory.domain.repository.StockReservationRepository;
import xbb.ai.erp.module.inventory.infrastructure.persistence.convertor.StockReservationConvertor;
import xbb.ai.erp.module.inventory.infrastructure.persistence.mapper.StockReservationMapper;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockReservationPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleInventoryStockReservationRepositoryImpl")
@RequiredArgsConstructor
public class StockReservationRepositoryImpl implements StockReservationRepository {

    private final StockReservationMapper stockReservationMapper;

    @Override
    public Long insert(StockReservation stockReservation) {
        StockReservationPO po = StockReservationConvertor.toPO(stockReservation);
        po.setId(null);
        stockReservationMapper.insert(po);
        stockReservation.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<StockReservation> stockReservationList) {
        List<StockReservationPO> poList = stockReservationList.stream().map(StockReservationConvertor::toPO).toList();
        poList.forEach(po -> po.setId(null));
        stockReservationMapper.insertBatch(poList);
        for (int index = 0; index < stockReservationList.size(); index++) {
            stockReservationList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        stockReservationMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        stockReservationMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(StockReservation stockReservation) {
        StockReservationPO po = StockReservationConvertor.toPO(stockReservation);
        stockReservationMapper.update(po);
    }

    @Override
    public StockReservation findById(String corpid, Long id) {
        return StockReservationConvertor.toDomain(stockReservationMapper.findById(corpid, id));
    }

    @Override
    public List<StockReservation> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return stockReservationMapper.findByCondition(preparedConditionMap).stream().map(StockReservationConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return stockReservationMapper.count(preparedConditionMap);
    }
}
