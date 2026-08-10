package xbb.ai.erp.module.demo.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.demo.domain.model.Demo;
import xbb.ai.erp.module.demo.domain.repository.DemoRepository;
import xbb.ai.erp.module.demo.infrastructure.persistence.convertor.DemoConvertor;
import xbb.ai.erp.module.demo.infrastructure.persistence.mapper.DemoMapper;
import xbb.ai.erp.module.demo.infrastructure.persistence.po.DemoPO;

import java.util.List;
import java.util.Map;
import java.util.Collection;

@Repository("xbbAiErpModuleDemoDemoRepositoryImpl")
@RequiredArgsConstructor
public class DemoRepositoryImpl implements DemoRepository {

    private final DemoMapper demoMapper;

    @Override
    public void insert(Demo demo) {
        demoMapper.insert(DemoConvertor.toPO(demo));
    }

    @Override
    public void insertBatch(List<Demo> demoList) {
        demoMapper.insertBatch(demoList.stream().map(DemoConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        demoMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        demoMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(Demo demo) {
        DemoPO po = DemoConvertor.toPO(demo);
        demoMapper.update(po);
    }

    @Override
    public Demo findById(String corpid, Long id) {
        return DemoConvertor.toDomain(demoMapper.findById(corpid, id));
    }

    @Override
    public List<Demo> findByIds(String corpid, Collection<Long> ids) {
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return demoMapper.findByIds(corpid, ids).stream().map(DemoConvertor::toDomain).toList();
    }

    @Override
    public List<Demo> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return demoMapper.findByCondition(preparedConditionMap).stream().map(DemoConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return demoMapper.count(preparedConditionMap);
    }
}
