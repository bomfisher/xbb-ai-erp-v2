package xbb.ai.erp.module.inventory.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.inventory.domain.model.StockBalance;
import xbb.ai.erp.module.inventory.domain.repository.StockBalanceRepository;
import xbb.ai.erp.module.inventory.infrastructure.persistence.convertor.StockBalanceConvertor;
import xbb.ai.erp.module.inventory.infrastructure.persistence.mapper.StockBalanceMapper;
import xbb.ai.erp.module.inventory.infrastructure.persistence.po.StockBalancePO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleInventoryStockBalanceRepositoryImpl")
@RequiredArgsConstructor
public class StockBalanceRepositoryImpl implements StockBalanceRepository {

    private final StockBalanceMapper stockBalanceMapper;

    @Override
    public Long insert(StockBalance stockBalance) {
        StockBalancePO po = StockBalanceConvertor.toPO(stockBalance);
        po.setId(null);
        stockBalanceMapper.insert(po);
        stockBalance.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<StockBalance> stockBalanceList) {
        List<StockBalancePO> poList = stockBalanceList.stream().map(StockBalanceConvertor::toPO).toList();
        poList.forEach(po -> po.setId(null));
        stockBalanceMapper.insertBatch(poList);
        for (int index = 0; index < stockBalanceList.size(); index++) {
            stockBalanceList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        stockBalanceMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        stockBalanceMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(StockBalance stockBalance) {
        StockBalancePO po = StockBalanceConvertor.toPO(stockBalance);
        stockBalanceMapper.update(po);
    }

    @Override
    public StockBalance findById(String corpid, Long id) {
        return StockBalanceConvertor.toDomain(stockBalanceMapper.findById(corpid, id));
    }

    public StockBalance findByWarehouseAndSku(String corpid, Long warehouseId, Long skuId) {
        return StockBalanceConvertor.toDomain(stockBalanceMapper.findByWarehouseAndSku(corpid, warehouseId, skuId));
    }

    @Override
    public List<StockBalance> findByWarehouseAndSkuPairs(String corpid, List<StockBalance> stockKeys) {
        List<StockBalancePO> keys = stockKeys.stream().map(StockBalanceConvertor::toPO).toList();
        return stockBalanceMapper.findByWarehouseAndSkuPairs(corpid, keys).stream().map(StockBalanceConvertor::toDomain).toList();
    }

    @Override
    public boolean updateWithVersion(StockBalance stockBalance, Integer expectedVersion) {
        return stockBalanceMapper.updateWithVersion(StockBalanceConvertor.toPO(stockBalance), expectedVersion) == 1;
    }

    @Override
    public List<StockBalance> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return stockBalanceMapper.findByCondition(preparedConditionMap).stream().map(StockBalanceConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return stockBalanceMapper.count(preparedConditionMap);
    }
}
