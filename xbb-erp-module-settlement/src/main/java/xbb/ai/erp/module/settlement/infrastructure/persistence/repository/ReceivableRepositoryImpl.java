package xbb.ai.erp.module.settlement.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.settlement.domain.model.Receivable;
import xbb.ai.erp.module.settlement.domain.repository.ReceivableRepository;
import xbb.ai.erp.module.settlement.infrastructure.persistence.convertor.ReceivableConvertor;
import xbb.ai.erp.module.settlement.infrastructure.persistence.mapper.ReceivableMapper;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.ReceivablePO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleSettlementReceivableRepositoryImpl")
@RequiredArgsConstructor
public class ReceivableRepositoryImpl implements ReceivableRepository {

    private final ReceivableMapper receivableMapper;

    @Override
    public Long insert(Receivable receivable) {
        ReceivablePO po = ReceivableConvertor.toPO(receivable);
        initializeForInsert(po);
        receivableMapper.insert(po);
        receivable.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<Receivable> receivableList) {
        List<ReceivablePO> poList = receivableList.stream().map(ReceivableConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        receivableMapper.insertBatch(poList);
        for (int index = 0; index < receivableList.size(); index++) {
            receivableList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        receivableMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        receivableMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(Receivable receivable) {
        ReceivablePO po = ReceivableConvertor.toPO(receivable);
        receivableMapper.update(po);
    }

    @Override
    public Receivable findById(String corpid, Long id) {
        return ReceivableConvertor.toDomain(receivableMapper.findById(corpid, id));
    }

    @Override
    public List<Receivable> findByIds(String corpid, java.util.Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return receivableMapper.findByIds(corpid, ids).stream().map(ReceivableConvertor::toDomain).toList();
    }

    @Override
    public List<Receivable> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return receivableMapper.findByCondition(preparedConditionMap).stream().map(ReceivableConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return receivableMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
