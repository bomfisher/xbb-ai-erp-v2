package xbb.ai.erp.module.demo.sub.domain.repository;

import java.util.List;
import java.util.Map;
import xbb.ai.erp.module.demo.sub.domain.model.DemoSub;

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
