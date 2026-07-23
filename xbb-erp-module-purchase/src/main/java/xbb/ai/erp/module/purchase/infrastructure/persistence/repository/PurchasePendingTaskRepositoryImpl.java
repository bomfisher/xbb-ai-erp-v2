package xbb.ai.erp.module.purchase.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.purchase.domain.model.PurchasePendingTask;
import xbb.ai.erp.module.purchase.domain.repository.PurchasePendingTaskRepository;
import xbb.ai.erp.module.purchase.infrastructure.persistence.convertor.PurchasePendingTaskConvertor;
import xbb.ai.erp.module.purchase.infrastructure.persistence.mapper.PurchasePendingTaskMapper;
import xbb.ai.erp.module.purchase.infrastructure.persistence.po.PurchasePendingTaskPO;

import java.util.List;
import java.util.Map;

@Repository
@RequiredArgsConstructor
public class PurchasePendingTaskRepositoryImpl implements PurchasePendingTaskRepository {

    private final PurchasePendingTaskMapper purchasePendingTaskMapper;

    @Override
    public void insert(PurchasePendingTask purchasePendingTask) {
        purchasePendingTaskMapper.insert(PurchasePendingTaskConvertor.toPO(purchasePendingTask));
    }

    @Override
    public void insertBatch(List<PurchasePendingTask> purchasePendingTaskList) {
        purchasePendingTaskMapper.insertBatch(purchasePendingTaskList.stream().map(PurchasePendingTaskConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        purchasePendingTaskMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        purchasePendingTaskMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(PurchasePendingTask purchasePendingTask) {
        PurchasePendingTaskPO po = PurchasePendingTaskConvertor.toPO(purchasePendingTask);
        purchasePendingTaskMapper.update(po);
    }

    @Override
    public PurchasePendingTask findById(String corpid, Long id) {
        return PurchasePendingTaskConvertor.toDomain(purchasePendingTaskMapper.findById(corpid, id));
    }

    @Override
    public List<PurchasePendingTask> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchasePendingTaskMapper.findByCondition(preparedConditionMap).stream().map(PurchasePendingTaskConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return purchasePendingTaskMapper.count(preparedConditionMap);
    }
}
