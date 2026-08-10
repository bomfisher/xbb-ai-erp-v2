package xbb.ai.erp.module.demo.infrastructure.persistence.repository;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import xbb.ai.erp.module.demo.domain.model.DemoItem;
import xbb.ai.erp.module.demo.domain.repository.DemoItemRepository;
import xbb.ai.erp.module.demo.infrastructure.persistence.convertor.DemoItemConvertor;
import xbb.ai.erp.module.demo.infrastructure.persistence.mapper.DemoItemMapper;
import xbb.ai.erp.module.demo.infrastructure.persistence.po.DemoItemPO;

import java.util.List;
import java.util.Map;

@Repository("xbbAiErpModuleDemoDemoItemRepositoryImpl")
@RequiredArgsConstructor
public class DemoItemRepositoryImpl implements DemoItemRepository {

    private final DemoItemMapper demoItemMapper;

    @Override
    public void insert(DemoItem demoItem) {
        demoItemMapper.insert(DemoItemConvertor.toPO(demoItem));
    }

    @Override
    public void insertBatch(List<DemoItem> demoItemList) {
        demoItemMapper.insertBatch(demoItemList.stream().map(DemoItemConvertor::toPO).toList());
    }

    @Override
    public void removeById(String corpid, Long id) {
        demoItemMapper.removeById(corpid, id);
    }

    @Override
    public void removeBatchByIds(String corpid, List<Long> ids) {
        demoItemMapper.removeBatchByIds(corpid, ids);
    }

    @Override
    public void update(DemoItem demoItem) {
        DemoItemPO po = DemoItemConvertor.toPO(demoItem);
        demoItemMapper.update(po);
    }

    @Override
    public DemoItem findById(String corpid, Long id) {
        return DemoItemConvertor.toDomain(demoItemMapper.findById(corpid, id));
    }

    @Override
    public List<DemoItem> findByCondition(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return demoItemMapper.findByCondition(preparedConditionMap).stream().map(DemoItemConvertor::toDomain).toList();
    }

    @Override
    public Long count(Map<String, Object> conditionMap) {
        Map<String, Object> preparedConditionMap = ConditionMapHelper.prepare(conditionMap);
        return demoItemMapper.count(preparedConditionMap);
    }
}
