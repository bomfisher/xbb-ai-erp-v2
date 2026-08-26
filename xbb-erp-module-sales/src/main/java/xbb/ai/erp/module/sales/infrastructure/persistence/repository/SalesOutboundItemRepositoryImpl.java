package xbb.ai.erp.module.sales.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.sales.domain.model.SalesOutboundItem;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundItemRepository;
import xbb.ai.erp.module.sales.infrastructure.persistence.convertor.SalesOutboundItemConvertor;
import xbb.ai.erp.module.sales.infrastructure.persistence.mapper.SalesOutboundItemMapper;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOutboundItemPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleSalesSalesOutboundItemRepositoryImpl")
@RequiredArgsConstructor
public class SalesOutboundItemRepositoryImpl implements SalesOutboundItemRepository {

    private final SalesOutboundItemMapper salesOutboundItemMapper;

    @Override
    public Long insert(SalesOutboundItem salesOutboundItem) {
        SalesOutboundItemPO po = SalesOutboundItemConvertor.toPO(salesOutboundItem);
        initializeForInsert(po);
        salesOutboundItemMapper.insert(po);
        salesOutboundItem.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<SalesOutboundItem> salesOutboundItemList) {
        List<SalesOutboundItemPO> poList = salesOutboundItemList.stream().map(SalesOutboundItemConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        salesOutboundItemMapper.insertBatch(poList);
        for (int index = 0; index < salesOutboundItemList.size(); index++) {
            salesOutboundItemList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        salesOutboundItemMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        salesOutboundItemMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(SalesOutboundItem salesOutboundItem) {
        SalesOutboundItemPO po = SalesOutboundItemConvertor.toPO(salesOutboundItem);
        salesOutboundItemMapper.update(po);
    }

    @Override
    public SalesOutboundItem findById(String corpid, Long id) {
        return SalesOutboundItemConvertor.toDomain(salesOutboundItemMapper.findById(corpid, id));
    }

    @Override
    public SalesOutboundItem findByIdForUpdate(String corpid, Long id) {
        return SalesOutboundItemConvertor.toDomain(salesOutboundItemMapper.findByIdForUpdate(corpid, id));
    }

    @Override
    public List<SalesOutboundItem> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesOutboundItemMapper.findByCondition(preparedConditionMap).stream().map(SalesOutboundItemConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesOutboundItemMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
