package xbb.ai.erp.module.demo.domain.repository;

import xbb.ai.erp.module.demo.domain.model.DemoItem;

import java.util.List;
import java.util.Map;

public interface DemoItemRepository {
    void insert(DemoItem demoItem);

    void insertBatch(List<DemoItem> demoItemList);

    void removeById(String corpid, Long id);

    void removeBatchByIds(String corpid, List<Long> ids);

    void update(DemoItem demoItem);

    DemoItem findById(String corpid, Long id);

    List<DemoItem> findByCondition(Map<String, Object> conditionMap);

    Long count(Map<String, Object> conditionMap);
}
