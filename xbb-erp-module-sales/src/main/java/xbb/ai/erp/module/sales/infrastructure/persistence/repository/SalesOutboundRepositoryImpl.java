package xbb.ai.erp.module.sales.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.sales.domain.model.SalesOutbound;
import xbb.ai.erp.module.sales.domain.repository.SalesOutboundRepository;
import xbb.ai.erp.module.sales.infrastructure.persistence.convertor.SalesOutboundConvertor;
import xbb.ai.erp.module.sales.infrastructure.persistence.mapper.SalesOutboundMapper;
import xbb.ai.erp.module.sales.infrastructure.persistence.po.SalesOutboundPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleSalesSalesOutboundRepositoryImpl")
@RequiredArgsConstructor
public class SalesOutboundRepositoryImpl implements SalesOutboundRepository {

    private final SalesOutboundMapper salesOutboundMapper;

    @Override
    public Long insert(SalesOutbound salesOutbound) {
        SalesOutboundPO po = SalesOutboundConvertor.toPO(salesOutbound);
        initializeForInsert(po);
        salesOutboundMapper.insert(po);
        salesOutbound.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<SalesOutbound> salesOutboundList) {
        List<SalesOutboundPO> poList = salesOutboundList.stream().map(SalesOutboundConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        salesOutboundMapper.insertBatch(poList);
        for (int index = 0; index < salesOutboundList.size(); index++) {
            salesOutboundList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        salesOutboundMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        salesOutboundMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(SalesOutbound salesOutbound) {
        SalesOutboundPO po = SalesOutboundConvertor.toPO(salesOutbound);
        salesOutboundMapper.update(po);
    }

    @Override
    public SalesOutbound findById(String corpid, Long id) {
        return SalesOutboundConvertor.toDomain(salesOutboundMapper.findById(corpid, id));
    }

    @Override
    public List<SalesOutbound> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesOutboundMapper.findByCondition(preparedConditionMap).stream().map(SalesOutboundConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return salesOutboundMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
