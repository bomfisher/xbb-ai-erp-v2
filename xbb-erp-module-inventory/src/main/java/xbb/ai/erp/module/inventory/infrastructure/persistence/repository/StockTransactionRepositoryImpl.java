package xbb.ai.erp.module.inventory.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.inventory.domain.model.StockTransaction;
import xbb.ai.erp.module.inventory.admin.dto.StockTransactionQueryDTO;
import xbb.ai.erp.module.inventory.domain.pojo.StockTransactionQueryPojo;
import xbb.ai.erp.module.inventory.domain.repository.StockTransactionRepository;
import xbb.ai.erp.module.inventory.infrastructure.persistence.convertor.StockTransactionConvertor;
import xbb.ai.erp.module.inventory.infrastructure.persistence.mapper.StockTransactionMapper;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockTransactionPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleInventoryStockTransactionRepositoryImpl")
@RequiredArgsConstructor
public class StockTransactionRepositoryImpl implements StockTransactionRepository {

    private final StockTransactionMapper stockTransactionMapper;

    @Override
    public List<StockTransactionQueryPojo> queryList(StockTransactionQueryDTO query, int offset, int pageSize) {
        return stockTransactionMapper.queryList(query, offset, pageSize);
    }

    @Override
    public Long queryCount(StockTransactionQueryDTO query) {
        return stockTransactionMapper.queryCount(query);
    }

    @Override
    public Long insert(StockTransaction stockTransaction) {
        StockTransactionPO po = StockTransactionConvertor.toPO(stockTransaction);
        po.setId(null);
        stockTransactionMapper.insert(po);
        stockTransaction.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<StockTransaction> stockTransactionList) {
        List<StockTransactionPO> poList = stockTransactionList.stream().map(StockTransactionConvertor::toPO).toList();
        poList.forEach(po -> po.setId(null));
        stockTransactionMapper.insertBatch(poList);
        for (int index = 0; index < stockTransactionList.size(); index++) {
            stockTransactionList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        stockTransactionMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        stockTransactionMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(StockTransaction stockTransaction) {
        StockTransactionPO po = StockTransactionConvertor.toPO(stockTransaction);
        stockTransactionMapper.update(po);
    }

    @Override
    public StockTransaction findById(String corpid, Long id) {
        return StockTransactionConvertor.toDomain(stockTransactionMapper.findById(corpid, id));
    }

    @Override
    public List<StockTransaction> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return stockTransactionMapper.findByCondition(preparedConditionMap).stream().map(StockTransactionConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return stockTransactionMapper.count(preparedConditionMap);
    }
}
