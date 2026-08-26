package xbb.ai.erp.module.settlement.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.base.persistence.entity.BaseEntity;
import xbb.ai.erp.module.settlement.domain.model.Payable;
import xbb.ai.erp.module.settlement.domain.repository.PayableRepository;
import xbb.ai.erp.module.settlement.infrastructure.persistence.convertor.PayableConvertor;
import xbb.ai.erp.module.settlement.infrastructure.persistence.mapper.PayableMapper;
import xbb.ai.erp.module.settlement.infrastructure.persistence.po.PayablePO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleSettlementPayableRepositoryImpl")
@RequiredArgsConstructor
public class PayableRepositoryImpl implements PayableRepository {

    private final PayableMapper payableMapper;

    @Override
    public Long insert(Payable payable) {
        PayablePO po = PayableConvertor.toPO(payable);
        initializeForInsert(po);
        payableMapper.insert(po);
        payable.setId(po.getId());
        return po.getId();
    }

    @Override
    public void insertBatch(List<Payable> payableList) {
        List<PayablePO> poList = payableList.stream().map(PayableConvertor::toPO).toList();
        poList.forEach(this::initializeForInsert);
        payableMapper.insertBatch(poList);
        for (int index = 0; index < payableList.size(); index++) {
            payableList.get(index).setId(poList.get(index).getId());
        }
    }

    @Override
    public void removeById(String corpid, Long id) {
        payableMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        payableMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(Payable payable) {
        PayablePO po = PayableConvertor.toPO(payable);
        payableMapper.update(po);
    }

    @Override
    public Payable findById(String corpid, Long id) {
        return PayableConvertor.toDomain(payableMapper.findById(corpid, id));
    }

    @Override
    public List<Payable> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return payableMapper.findByCondition(preparedConditionMap).stream().map(PayableConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return payableMapper.count(preparedConditionMap);
    }

    private void initializeForInsert(BaseEntity po) {
        long now = System.currentTimeMillis();
        po.setId(null);
        po.setDel(0);
        po.setAddTime(now);
        po.setUpdateTime(now);
    }
}
