package xbb.ai.erp.module.sales.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.sales.domain.model.SalesOrder;
import xbb.ai.erp.module.sales.domain.repository.SalesOrderRepository;
import xbb.ai.erp.module.sales.infrastructure.persistence.convertor.SalesOrderConvertor;
import xbb.ai.erp.module.sales.infrastructure.persistence.mapper.SalesOrderMapper;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOrderPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleSalesSalesOrderRepositoryImpl")
@RequiredArgsConstructor
public class SalesOrderRepositoryImpl implements SalesOrderRepository {

    private final SalesOrderMapper salesOrderMapper;

    @Override
    public Long insert(SalesOrder salesOrder) {
        SalesOrderPO po = SalesOrderConvertor.toPO(salesOrder);
        initializeForInsert(po);
        salesOrderMapper.insert(po);
        salesOrder.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<SalesOrder> salesOrderList) {
        List<SalesOrderPO> poList = salesOrderList.stream().map(SalesOrderConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        salesOrderMapper.insertBatch(poList);
        for (int index = 0; index < salesOrderList.size(); index++) {
            salesOrderList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        salesOrderMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        salesOrderMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(SalesOrder salesOrder) {
        SalesOrderPO po = SalesOrderConvertor.toPO(salesOrder);
        po.setUpdateTime(System.currentTimeMillis());
        salesOrderMapper.update(po);
    }

    @Override
    public SalesOrder findById(String corpid, Long id) {
        return SalesOrderConvertor.toDomain(salesOrderMapper.findById(corpid, id));
    }

    @Override
    public List<SalesOrder> findByIds(String corpid, java.util.Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return salesOrderMapper.findByIds(corpid, ids).stream().map(SalesOrderConvertor::toDomain).toList();
    }

    @Override
    public List<SalesOrder> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesOrderMapper.findByCondition(preparedConditionMap).stream().map(SalesOrderConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesOrderMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
