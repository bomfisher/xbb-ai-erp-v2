package xbb.ai.erp.module.inventory.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.inventory.domain.model.StockCostTransaction;
import xbb.ai.erp.module.inventory.domain.repository.StockCostTransactionRepository;
import xbb.ai.erp.module.inventory.infrastructure.persistence.convertor.StockCostTransactionConvertor;
import xbb.ai.erp.module.inventory.infrastructure.persistence.mapper.StockCostTransactionMapper;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockCostTransactionPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleInventoryStockCostTransactionRepositoryImpl")
@RequiredArgsConstructor
public class StockCostTransactionRepositoryImpl implements StockCostTransactionRepository {

    private final StockCostTransactionMapper stockCostTransactionMapper;

    @Override
    public Long insert(StockCostTransaction stockCostTransaction) {
        StockCostTransactionPO po = StockCostTransactionConvertor.toPO(stockCostTransaction);
        po.setId(null);
        stockCostTransactionMapper.insert(po);
        stockCostTransaction.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<StockCostTransaction> stockCostTransactionList) {
        List<StockCostTransactionPO> poList = stockCostTransactionList.stream().map(StockCostTransactionConvertor::toPO).toList();
        poList.forEach(po -> po.setId(null));
        stockCostTransactionMapper.insertBatch(poList);
        for (int index = 0; index < stockCostTransactionList.size(); index++) {
            stockCostTransactionList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        stockCostTransactionMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        stockCostTransactionMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(StockCostTransaction stockCostTransaction) {
        StockCostTransactionPO po = StockCostTransactionConvertor.toPO(stockCostTransaction);
        stockCostTransactionMapper.update(po);
    }

    @Override
    public StockCostTransaction findById(String corpid, Long id) {
        return StockCostTransactionConvertor.toDomain(stockCostTransactionMapper.findById(corpid, id));
    }

    public StockCostTransaction findByIdempotencyKey(String corpid, String idempotencyKey) {
        return StockCostTransactionConvertor.toDomain(stockCostTransactionMapper.findByIdempotencyKey(corpid, idempotencyKey));
    }

    @Override
    public List<StockCostTransaction> findByIdempotencyKeys(String corpid, List<String> idempotencyKeys) {
        return stockCostTransactionMapper.findByIdempotencyKeys(corpid, idempotencyKeys).stream()
            .map(StockCostTransactionConvertor::toDomain).toList();
    }

    @Override
    public List<StockCostTransaction> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return stockCostTransactionMapper.findByCondition(preparedConditionMap).stream().map(StockCostTransactionConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return stockCostTransactionMapper.count(preparedConditionMap);
    }
}
