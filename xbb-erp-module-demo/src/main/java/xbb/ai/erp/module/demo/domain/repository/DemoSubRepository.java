package xbb.ai.erp.module.demo.domain.repository;

import xbb.ai.erp.module.demo.domain.model.DemoSub;

import java.util.List;
import java.util.Map;

public interface DemoSubRepository {
    void insert(DemoSub demoSub);

    void insertBatch(List<DemoSub> demoSubList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(DemoSub demoSub);

    DemoSub findById(String corpid, Long id);

    List<DemoSub> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
