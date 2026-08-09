package xbb.ai.erp.module.demo.sub.infrastructure.persistence.repository;

import java.util.List;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.demo.sub.domain.model.DemoSub;
import xbb.ai.erp.module.demo.sub.domain.repository.DemoSubRepository;
import xbb.ai.erp.module.demo.sub.infrastructure.persistence.convertor.DemoSubConvertor;
import xbb.ai.erp.module.demo.sub.infrastructure.persistence.mapper.DemoSubMapper;
import xbb.ai.erp.module.demo.sub.infrastructure.persistence.po.DemoSubPO;

@Repository("xbbAiErpModuleDemoSubDemoSubRepositoryImpl")
@RequiredArgsConstructor
public class DemoSubRepositoryImpl implements DemoSubRepository {

  private final DemoSubMapper demoSubMapper;

  @Override
  public void insert(DemoSub demoSub) {
    demoSubMapper.insert(DemoSubConvertor.toPO(demoSub));
  }

  @Override
  public void insertBatch(List<DemoSub> demoSubList) {
    demoSubMapper.insertBatch(demoSubList.stream().map(DemoSubConvertor::toPO).toList());
  }

  @Override
  public void removeById(String corpid, Long id) {
    demoSubMapper.removeById(corpid, id);
  }

  @Override
  public void removeBatchByIds(String corpid, List<Long> ids) {
    demoSubMapper.removeBatchByIds(corpid, ids);
  }

  @Override
  public void update(DemoSub demoSub) {
    DemoSubPO po = DemoSubConvertor.toPO(demoSub);
    demoSubMapper.update(po);
  }

  @Override
  public DemoSub findById(String corpid, Long id) {
    return DemoSubConvertor.toDomain(demoSubMapper.findById(corpid, id));
  }

  @Override
  public List<DemoSub> findByCondition(Map<String, Object> conditionMap) {
    Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
    return demoSubMapper.findByCondition(preparedConditionMap).stream()
        .map(DemoSubConvertor::toDomain)
        .toList();
  }

  @Override
  public Long count(Map<String, Object> conditionMap) {
    Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
    return demoSubMapper.count(preparedConditionMap);
  }
}
