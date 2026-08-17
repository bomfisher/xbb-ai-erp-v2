package xbb.ai.erp.module.sales.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.sales.domain.model.SalesOrderItem;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderItemRepository;
import xbb.ai.erp.module.sales.infrastructure.persistence.convertor.SalesOrderItemConvertor;
import xbb.ai.erp.module.sales.infrastructure.persistence.mapper.SalesOrderItemMapper;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOrderItemPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleSalesSalesOrderItemRepositoryImpl")
@RequiredArgsConstructor
public class SalesOrderItemRepositoryImpl implements SalesOrderItemRepository {

    private final SalesOrderItemMapper salesOrderItemMapper;

    @Override
    public Long insert(SalesOrderItem salesOrderItem) {
        SalesOrderItemPO po = SalesOrderItemConvertor.toPO(salesOrderItem);
        initializeForInsert(po);
        salesOrderItemMapper.insert(po);
        salesOrderItem.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<SalesOrderItem> salesOrderItemList) {
        List<SalesOrderItemPO> poList = salesOrderItemList.stream().map(SalesOrderItemConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        salesOrderItemMapper.insertBatch(poList);
        for (int index = 0; index < salesOrderItemList.size(); index++) {
            salesOrderItemList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        salesOrderItemMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        salesOrderItemMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(SalesOrderItem salesOrderItem) {
        SalesOrderItemPO po = SalesOrderItemConvertor.toPO(salesOrderItem);
        po.setUpdateTime(System.currentTimeMillis());
        salesOrderItemMapper.update(po);
    }

    @Override
    public SalesOrderItem findById(String corpid, Long id) {
        return SalesOrderItemConvertor.toDomain(salesOrderItemMapper.findById(corpid, id));
    }

    @Override
    public List<SalesOrderItem> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesOrderItemMapper.findByCondition(preparedConditionMap).stream().map(SalesOrderItemConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesOrderItemMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
