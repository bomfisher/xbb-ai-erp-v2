package xbb.ai.erp.module.demo.domain.repository;

import xbb.ai.erp.module.demo.domain.model.Demo;

import java.util.List;
import java.util.Map;

public interface DemoRepository {
    void insert(Demo demo);

    void insertBatch(List<Demo> demoList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(Demo demo);

    Demo findById(String corpid, Long id);

    List<Demo> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
