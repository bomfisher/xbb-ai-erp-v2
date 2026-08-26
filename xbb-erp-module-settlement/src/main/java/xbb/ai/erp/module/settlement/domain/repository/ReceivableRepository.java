package xbb.ai.erp.module.settlement.domain.repository;

import xbb.ai.erp.module.settlement.domain.model.Receivable;

import java.util.List;
import java.util.Map;

public interface ReceivableRepository {
    Long insert(Receivable receivable);

    void insertBatch(List<Receivable> receivableList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(Receivable receivable);

    Receivable findById(String corpid, Long id);

    List<Receivable> findByIds(String corpid, java.util.Collection<Long> ids);

    List<Receivable> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
