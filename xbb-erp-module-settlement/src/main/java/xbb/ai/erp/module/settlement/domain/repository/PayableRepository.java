package xbb.ai.erp.module.settlement.domain.repository;

import xbb.ai.erp.module.settlement.domain.model.Payable;

import java.util.List;
import java.util.Map;

public interface PayableRepository {
    Long insert(Payable payable);

    void insertBatch(List<Payable> payableList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(Payable payable);

    Payable findById(String corpid, Long id);

    List<Payable> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
